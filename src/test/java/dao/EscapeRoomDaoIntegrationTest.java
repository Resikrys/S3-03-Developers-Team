// src/test/java/dao/EscapeRoomDaoIntegrationTest.java
package dao;

import model.EscapeRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class EscapeRoomDaoIntegrationTest {

    private EscapeRoomDaoImpl escapeRoomDao;

    @BeforeEach
    void setUp() {
        // Usar la implementación real sin mocks
        escapeRoomDao = new EscapeRoomDaoImpl();
    }

    @Test
    void createEscapeRoom_Success() throws SQLException {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom("Test Room", 50);

        // Act
        escapeRoomDao.createEscaperoom(escapeRoom);

        // Assert
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        assertFalse(allRooms.isEmpty());

        // Verificar que se creó correctamente
        Optional<EscapeRoom> createdRoom = allRooms.stream()
                .filter(room -> room.getName().equals("Test Room"))
                .findFirst();
        assertTrue(createdRoom.isPresent());
        assertEquals(50, createdRoom.get().getTotalTickets());
    }

    @Test
    void getEscapeRoomById_WhenExists_ReturnsEscapeRoom() throws SQLException {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom("Test Room", 50);
        escapeRoomDao.createEscaperoom(escapeRoom);

        // Obtener el ID generado
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        int createdId = allRooms.stream()
                .filter(room -> room.getName().equals("Test Room"))
                .findFirst()
                .orElseThrow()
                .getId();

        // Act
        Optional<EscapeRoom> result = escapeRoomDao.getEscapeRoomById(createdId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Room", result.get().getName());
        assertEquals(50, result.get().getTotalTickets());
    }

    @Test
    void getEscapeRoomById_WhenNotExists_ReturnsEmpty() throws SQLException {
        // Act
        Optional<EscapeRoom> result = escapeRoomDao.getEscapeRoomById(999);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void getAll_ReturnsAllEscapeRooms() throws SQLException {
        // Arrange
        EscapeRoom room1 = new EscapeRoom("Room 1", 30);
        EscapeRoom room2 = new EscapeRoom("Room 2", 40);
        escapeRoomDao.createEscaperoom(room1);
        escapeRoomDao.createEscaperoom(room2);

        // Act
        List<EscapeRoom> result = escapeRoomDao.getAll();

        // Assert
        assertTrue(result.size() >= 2);
        assertTrue(result.stream().anyMatch(room -> room.getName().equals("Room 1")));
        assertTrue(result.stream().anyMatch(room -> room.getName().equals("Room 2")));
    }

    @Test
    void updateEscapeRoom_Success() throws SQLException {
        // Arrange
        EscapeRoom originalRoom = new EscapeRoom("Original Name", 50);
        escapeRoomDao.createEscaperoom(originalRoom);

        // Obtener el ID generado
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        int createdId = allRooms.stream()
                .filter(room -> room.getName().equals("Original Name"))
                .findFirst()
                .orElseThrow()
                .getId();

        EscapeRoom updatedRoom = new EscapeRoom(createdId, "Updated Name", 75);

        // Act
        escapeRoomDao.updateEscaperoom(updatedRoom);

        // Assert
        Optional<EscapeRoom> result = escapeRoomDao.getEscapeRoomById(createdId);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals(75, result.get().getTotalTickets());
    }

    @Test
    void deleteEscapeRoom_Success() throws SQLException {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom("Room to Delete", 25);
        escapeRoomDao.createEscaperoom(escapeRoom);

        // Obtener el ID generado
        List<EscapeRoom> allRooms = escapeRoomDao.getAll();
        int createdId = allRooms.stream()
                .filter(room -> room.getName().equals("Room to Delete"))
                .findFirst()
                .orElseThrow()
                .getId();

        // Act
        escapeRoomDao.deleteEscaperoom(createdId);

        // Assert
        Optional<EscapeRoom> result = escapeRoomDao.getEscapeRoomById(createdId);
        assertFalse(result.isPresent());
    }
}