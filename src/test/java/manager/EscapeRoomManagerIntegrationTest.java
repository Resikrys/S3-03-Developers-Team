// src/test/java/manager/EscapeRoomManagerIntegrationTest.java
package manager;

import dao.EscapeRoomDAO;
import dao.EscapeRoomDaoImpl;
import model.EscapeRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.InputHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EscapeRoomManagerIntegrationTest {

    private EscapeRoomManager escapeRoomManager;
    private InputHelper inputHelper;

    @BeforeEach
    void setUp() {
        // Configurar System.in para simular entrada del usuario
        String input = "Test Room\n50\n"; // nombre + total_tickets
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        inputHelper = new InputHelper();
        escapeRoomManager = new EscapeRoomManager(inputHelper);
    }

    @Test
    void createEscapeRoom_WithValidInput_Success() throws SQLException {
        // Act
        escapeRoomManager.createEscapeRoom();

        // Assert - Verificar que se creó en la base de datos
        // Esto requiere acceso al DAO para verificar
        // Puedes agregar un método getter en el manager o usar reflection
    }

    @Test
    void listAllEscapeRooms_DisplaysRooms() throws SQLException {
        // Arrange - Crear algunos escape rooms primero
        createTestEscapeRoom("Room 1", 30);
        createTestEscapeRoom("Room 2", 40);

        // Act
        escapeRoomManager.listAllEscapeRooms();

        // Assert - Verificar que se muestran los rooms
        // Esto es más difícil de testear sin capturar System.out
    }

    private void createTestEscapeRoom(String name, int totalTickets) throws SQLException {
        // Método auxiliar para crear escape rooms de prueba
        EscapeRoom escapeRoom = new EscapeRoom(name, totalTickets);
        // Aquí necesitarías acceso al DAO, o podrías agregar un método en el manager
    }
}