// src/test/java/model/EscapeRoomTest.java
package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EscapeRoomTest {

    @Test
    void constructor_WithValidData_CreatesEscapeRoom() {
        // Act
        EscapeRoom escapeRoom = new EscapeRoom("Test Room", 50);

        // Assert
        assertEquals("Test Room", escapeRoom.getName());
        assertEquals(50, escapeRoom.getTotalTickets());
        assertEquals(0, escapeRoom.getId());
    }

    @Test
    void constructor_WithId_CreatesEscapeRoomWithId() {
        // Act
        EscapeRoom escapeRoom = new EscapeRoom(1, "Test Room", 50);

        // Assert
        assertEquals(1, escapeRoom.getId());
        assertEquals("Test Room", escapeRoom.getName());
        assertEquals(50, escapeRoom.getTotalTickets());
    }

    @Test
    void setters_UpdateValuesCorrectly() {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom("Old Name", 30);

        // Act
        escapeRoom.setId(5);
        escapeRoom.setName("New Name");
        escapeRoom.setTotalTickets(75);

        // Assert
        assertEquals(5, escapeRoom.getId());
        assertEquals("New Name", escapeRoom.getName());
        assertEquals(75, escapeRoom.getTotalTickets());
    }

    @Test
    void equals_WithSameId_ReturnsTrue() {
        // Arrange
        EscapeRoom room1 = new EscapeRoom(1, "Room 1", 30);
        EscapeRoom room2 = new EscapeRoom(1, "Room 2", 40);

        // Act & Assert
        assertEquals(room1, room2);
    }

    @Test
    void toString_ContainsAllFields() {
        // Arrange
        EscapeRoom escapeRoom = new EscapeRoom(1, "Test Room", 50);

        // Act
        String result = escapeRoom.toString();

        // Assert
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Test Room"));
        assertTrue(result.contains("50"));
    }
}