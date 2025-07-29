package dbc;


import dao.EscapeRoomDaoImpl;
import model.EscapeRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class EscapeRoomDatabaseTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private EscapeRoomDaoImpl escapeRoomDao;

    @BeforeEach
    void setUp() throws SQLException {
        // Configurar base de datos de prueba
        setupTestDatabase();
        escapeRoomDao = new EscapeRoomDaoImpl();
    }

    private void setupTestDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                mysqlContainer.getJdbcUrl(),
                mysqlContainer.getUsername(),
                mysqlContainer.getPassword())) {

            try (Statement statement = connection.createStatement()) {
                // Crear tabla de prueba
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS EscapeRoom (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        total_tickets INT NOT NULL
                    )
                """);

                // Limpiar datos de pruebas anteriores
                statement.execute("DELETE FROM EscapeRoom");
            }
        }
    }

    @Test
    void createEscapeRoom_WithRealDatabase_Success() throws SQLException {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom("Real Test Room", 100);

        // Act
        escapeRoomDao.createEscaperoom(escapeRoom);

        // Assert
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        assertFalse(allRooms.isEmpty());

        Optional<EscapeRoom> createdRoom = allRooms.stream()
                .filter(room -> room.getName().equals("Real Test Room"))
                .findFirst();
        assertTrue(createdRoom.isPresent());
        assertEquals(100, createdRoom.get().getTotalTickets());
    }

    @Test
    void fullCrudOperations_WithRealDatabase_Success() throws SQLException {
        // Create
        EscapeRoom escapeRoom = new EscapeRoom("CRUD Test Room", 50);
        escapeRoomDao.createEscaperoom(escapeRoom);

        // Read
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        Optional<EscapeRoom> createdRoom = allRooms.stream()
                .filter(room -> room.getName().equals("CRUD Test Room"))
                .findFirst();
        assertTrue(createdRoom.isPresent());

        // Update
        int roomId = createdRoom.get().getId();
        EscapeRoom updatedRoom = new EscapeRoom(roomId, "Updated CRUD Room", 75);
        escapeRoomDao.updateEscaperoom(updatedRoom);

        Optional<EscapeRoom> updatedResult = escapeRoomDao.getEscapeRoomById(roomId);
        assertTrue(updatedResult.isPresent());
        assertEquals("Updated CRUD Room", updatedResult.get().getName());
        assertEquals(75, updatedResult.get().getTotalTickets());

        // Delete
        escapeRoomDao.deleteEscaperoom(roomId);
        Optional<EscapeRoom> deletedResult = escapeRoomDao.getEscapeRoomById(roomId);
        assertFalse(deletedResult.isPresent());
    }
}