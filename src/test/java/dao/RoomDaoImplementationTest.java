package dao;

import model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DatabaseConnection;
import util.EnvLoader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RoomDaoImplementationTest {
    private RoomDao roomDao;

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Asegúrate de que las variables de entorno y la conexión se carguen
        EnvLoader.getInstance();
        DatabaseConnection.getInstance(); // Asegura que la instancia de conexión esté lista.

        roomDao = new RoomDaoImplementation();

        // 2. Limpiar la tabla Room antes de cada test para asegurar un estado conocido
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            // Eliminar dependientes si existen y luego eliminar Rooms
            stmt.executeUpdate("DELETE FROM DecorationObject");
            stmt.executeUpdate("DELETE FROM ClueObject");
            stmt.executeUpdate("DELETE FROM Player"); // Si Player puede tener escape_room_id nulo o si las rooms no se eliminan primero
            stmt.executeUpdate("DELETE FROM Ticket"); // Si Ticket referencia Room directamente o indirectamente
            stmt.executeUpdate("DELETE FROM Room");
            stmt.executeUpdate("ALTER TABLE Room AUTO_INCREMENT = 1"); // Reiniciar el contador de ID

            // También podemos recrear EscapeRooms para tener IDs conocidos
            stmt.executeUpdate("DELETE FROM EscapeRoom");
            stmt.executeUpdate("ALTER TABLE EscapeRoom AUTO_INCREMENT = 1");
            stmt.executeUpdate("INSERT INTO EscapeRoom (name, total_tickets) VALUES ('Test EscapeRoom 1', 10), ('Test EscapeRoom 2', 20)");
            System.out.println("Limpieza y setup inicial de tablas para el test de RoomDao.");
        }
    }

    @AfterEach
        // Se ejecuta después de cada método de test
    void tearDown() throws SQLException {
        // Opcional: Puedes limpiar las tablas de nuevo aquí si quieres,
        // aunque @BeforeEach ya las limpia antes de cada test.
        // No es necesario cerrar la conexión aquí si DatabaseConnection es un Singleton
        // y se cerrará una vez al final de todos los tests (en @AfterAll en DatabaseConnectionTest).
        System.out.println("Limpieza de recursos después de cada test de RoomDao.");
    }

    // --- Tests para el CRUD de Room ---

    @Test
    void testCreateRoom() throws SQLException {
        System.out.println("Ejecutando testCreateRoom...");
        Room newRoom = new Room("El Laboratorio del Mad Scientist", 8, 1); // Asociado a Test EscapeRoom 1
        roomDao.createRoom(newRoom);

        // Verifica que la Room fue creada leyendo de la DB
        List<Room> rooms = roomDao.getAllRooms();
        assertFalse(rooms.isEmpty(), "La lista de rooms no debería estar vacía después de crear una.");
        assertEquals(1, rooms.size(), "Debería haber 1 room en la base de datos.");

        Room retrievedRoom = rooms.get(0);
        assertNotNull(retrievedRoom.getId(), "El ID de la room no debería ser nulo después de la creación.");
        assertEquals("El Laboratorio del Mad Scientist", retrievedRoom.getTheme());
        assertEquals(8, retrievedRoom.getDifficultyLevel());
        assertEquals(1, retrievedRoom.getEscapeRoomId());
        System.out.println("testCreateRoom PASSED.");
    }

    @Test
    void testCreateRoomWithNullEscapeRoomId() throws SQLException {
        System.out.println("Ejecutando testCreateRoomWithNullEscapeRoomId...");
        Room newRoom = new Room("La Cabaña del Bosque", 6, null); // Sin EscapeRoom asociado
        roomDao.createRoom(newRoom);

        List<Room> rooms = roomDao.getAllRooms();
        assertEquals(1, rooms.size(), "Debería haber 1 room en la base de datos.");
        Room retrievedRoom = rooms.get(0);

        assertEquals("La Cabaña del Bosque", retrievedRoom.getTheme());
        assertEquals(6, retrievedRoom.getDifficultyLevel());
        assertNull(retrievedRoom.getEscapeRoomId(), "El escapeRoomId debería ser nulo.");
        System.out.println("testCreateRoomWithNullEscapeRoomId PASSED.");
    }

    @Test
    void testGetRoomById() throws SQLException {
        System.out.println("Ejecutando testGetRoomById...");
        // Primero, crea una room para poder buscarla
        Room createdRoom = new Room("El Misterio de la Pirámide", 7, 2); // Asociado a Test EscapeRoom 2
        roomDao.createRoom(createdRoom);

        // Asumiendo que el ID será 1 (por AUTO_INCREMENT y limpieza previa)
        Optional<Room> foundRoomOptional = roomDao.getRoomById(1);

        assertTrue(foundRoomOptional.isPresent(), "Debería encontrar la room con ID 1.");
        Room foundRoom = foundRoomOptional.get();
        assertEquals("El Misterio de la Pirámide", foundRoom.getTheme());
        assertEquals(7, foundRoom.getDifficultyLevel());
        assertEquals(2, foundRoom.getEscapeRoomId());
        System.out.println("testGetRoomById PASSED.");
    }

    @Test
    void testGetRoomByIdNotFound() throws SQLException {
        System.out.println("Ejecutando testGetRoomByIdNotFound...");
        Optional<Room> foundRoom = roomDao.getRoomById(999); // Un ID que no existe
        assertFalse(foundRoom.isPresent(), "No debería encontrar una room con un ID inexistente.");
        System.out.println("testGetRoomByIdNotFound PASSED.");
    }

    @Test
    void testGetAllRooms() throws SQLException {
        System.out.println("Ejecutando testGetAllRooms...");
        assertTrue(roomDao.getAllRooms().isEmpty(), "La lista debería estar vacía al inicio del test.");

        roomDao.createRoom(new Room("Test Room 1", 5, 1));
        roomDao.createRoom(new Room("Test Room 2", 6, 2));

        List<Room> rooms = roomDao.getAllRooms();
        assertFalse(rooms.isEmpty(), "La lista de rooms no debería estar vacía.");
        assertEquals(2, rooms.size(), "Debería haber 2 rooms en la base de datos.");
        System.out.println("testGetAllRooms PASSED.");
    }

    @Test
    void testUpdateRoom() throws SQLException {
        System.out.println("Ejecutando testUpdateRoom...");
        Room initialRoom = new Room("Room Original", 5, 1);
        roomDao.createRoom(initialRoom);

        // Obtener la room para obtener su ID
        Optional<Room> createdRoomOptional = roomDao.getAllRooms().stream().findFirst();
        assertTrue(createdRoomOptional.isPresent(), "La room debería existir para actualizar.");
        Room roomToUpdate = createdRoomOptional.get();

        // Modificar la room
        roomToUpdate.setTheme("Room Modificada");
        roomToUpdate.setDifficultyLevel(9);
        roomToUpdate.setEscapeRoomId(2); // Cambiar a otro EscapeRoom

        roomDao.updateRoom(roomToUpdate);

        // Recuperar la room actualizada y verificar
        Optional<Room> updatedRoomOptional = roomDao.getRoomById(roomToUpdate.getId());
        assertTrue(updatedRoomOptional.isPresent(), "La room actualizada debería poder ser recuperada.");
        Room updatedRoom = updatedRoomOptional.get();

        assertEquals("Room Modificada", updatedRoom.getTheme());
        assertEquals(9, updatedRoom.getDifficultyLevel());
        assertEquals(2, updatedRoom.getEscapeRoomId());
        System.out.println("testUpdateRoom PASSED.");
    }

    @Test
    void testUpdateRoomNotFound() throws SQLException {
        System.out.println("Ejecutando testUpdateRoomNotFound...");
        Room nonExistentRoom = new Room(999, "No Existe", 1, 1); // ID que no existe
        // Verificamos que no lance excepción y que el mensaje indique que no se encontró
        assertDoesNotThrow(() -> roomDao.updateRoom(nonExistentRoom));
        // Para verificar el mensaje de consola, necesitaríamos capturar la salida estándar,
        // lo cual es más avanzado. Por ahora, el "assertDoesNotThrow" es suficiente para el test.
        System.out.println("testUpdateRoomNotFound PASSED (no lanza excepción para Room inexistente).");
    }

    @Test
    void testDeleteRoom() throws SQLException {
        System.out.println("Ejecutando testDeleteRoom...");
        Room roomToDelete = new Room("Room a Borrar", 4, 1);
        roomDao.createRoom(roomToDelete);

        // Obtener el ID de la room creada
        Optional<Room> createdRoomOptional = roomDao.getAllRooms().stream().findFirst();
        assertTrue(createdRoomOptional.isPresent(), "La room debería existir para ser borrada.");
        int idToDelete = createdRoomOptional.get().getId();

        roomDao.deleteRoom(idToDelete);

        // Verificar que la room ya no existe
        Optional<Room> deletedRoom = roomDao.getRoomById(idToDelete);
        assertFalse(deletedRoom.isPresent(), "La room debería haber sido eliminada.");
        System.out.println("testDeleteRoom PASSED.");
    }

    @Test
    void testDeleteRoomNotFound() throws SQLException {
        System.out.println("Ejecutando testDeleteRoomNotFound...");
        assertDoesNotThrow(() -> roomDao.deleteRoom(999), "Eliminar una room inexistente no debería lanzar excepción.");
        System.out.println("testDeleteRoomNotFound PASSED.");
    }
}