package dao;

import exception.RoomNotFoundException;
import model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;

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
        EnvLoader.getInstance();
        DatabaseConnection.getInstance();

        roomDao = new RoomDaoImplementation();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            // --- Corrected Cleanup Order ---
            // 1. Delete from 'grandchild' tables first (Ticket, ClueObject, DecorationObject)
            stmt.executeUpdate("DELETE FROM Ticket");
            stmt.executeUpdate("DELETE FROM ClueObject");
            stmt.executeUpdate("DELETE FROM DecorationObject");

            // 2. Then delete from 'child' tables (Player, Room)
            stmt.executeUpdate("DELETE FROM Player");
            stmt.executeUpdate("DELETE FROM Room");

            // 3. Finally, delete from 'parent' table (EscapeRoom)
            stmt.executeUpdate("DELETE FROM EscapeRoom");

            // --- Reset AUTO_INCREMENT for all tables that use it ---
            // It's good practice to reset all related AUTO_INCREMENTs for clean test state
            stmt.executeUpdate("ALTER TABLE Ticket AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE ClueObject AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE DecorationObject AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE Player AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE Room AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE EscapeRoom AUTO_INCREMENT = 1");

            // --- Re-insert necessary data for tests ---
            // You only need EscapeRoom data for Room tests since Room has a FK to EscapeRoom
            // Other tables (Player, Ticket, ClueObject, DecorationObject) can be re-inserted
            // by specific tests if they are needed, or only when you test their respective DAOs.
            stmt.executeUpdate("INSERT INTO EscapeRoom (name, total_tickets) VALUES ('Test EscapeRoom 1', 10), ('Test EscapeRoom 2', 20)");
            System.out.println("Cleaning and initial setup of tables for the RoomDao test.");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        System.out.println("Resource cleanup after each RoomDao test.");
    }


    @Test
    void testCreateRoom() throws SQLException {
        System.out.println("Running testCreateRoom...");
        Room newRoom = new Room("The Mad Scientist's Laboratory", 8, 1);
        roomDao.createRoom(newRoom);

        List<Room> rooms = roomDao.getAllRooms();
        assertFalse(rooms.isEmpty(), "The list of rooms should not be empty after creating one.");
        assertEquals(1, rooms.size(), "There should be 1 room in the database.");

        Room retrievedRoom = rooms.get(0);
        assertNotNull(retrievedRoom.getId(), "Room ID should not be null after creation.");
        assertEquals("The Mad Scientist's Laboratory", retrievedRoom.getTheme());
        assertEquals(8, retrievedRoom.getDifficultyLevel());
        assertEquals(1, retrievedRoom.getEscapeRoomId());
        System.out.println("testCreateRoom PASSED.");
    }

    @Test
    void testCreateRoomWithNullEscapeRoomId() throws SQLException {
        System.out.println("Running testCreateRoomWithNullEscapeRoomId...");
        Room newRoom = new Room("The Forest Cabin", 6, null);
        roomDao.createRoom(newRoom);

        List<Room> rooms = roomDao.getAllRooms();
        assertEquals(1, rooms.size(), "There should be 1 room in the database.");
        Room retrievedRoom = rooms.get(0);

        assertEquals("The Forest Cabin", retrievedRoom.getTheme());
        assertEquals(6, retrievedRoom.getDifficultyLevel());
        assertNull(retrievedRoom.getEscapeRoomId(), "The escapeRoomId should be null.");
        System.out.println("testCreateRoomWithNullEscapeRoomId PASSED.");
    }

    @Test
    void testGetRoomById() throws SQLException {
        System.out.println("Running testGetRoomById...");
        Room createdRoom = new Room("The Mystery of the Pyramid", 7, 2);
        roomDao.createRoom(createdRoom);

        Optional<Room> foundRoomOptional = roomDao.getRoomById(1);

        assertTrue(foundRoomOptional.isPresent(), "Should find the room with ID 1.");
        Room foundRoom = foundRoomOptional.get();
        assertEquals("The Mystery of the Pyramid", foundRoom.getTheme());
        assertEquals(7, foundRoom.getDifficultyLevel());
        assertEquals(2, foundRoom.getEscapeRoomId());
        System.out.println("testGetRoomById PASSED.");
    }

    @Test
    void testGetRoomByIdNotFound() throws SQLException {
        System.out.println("Running testGetRoomByIdNotFound...");
        Optional<Room> foundRoom = roomDao.getRoomById(999);
        assertFalse(foundRoom.isPresent(), "It shouldn't find a room with a nonexistent ID.");
        System.out.println("testGetRoomByIdNotFound PASSED.");
    }

    @Test
    void testGetAllRooms() throws SQLException {
        System.out.println("Running testGetAllRooms...");
        assertTrue(roomDao.getAllRooms().isEmpty(), "The list should be empty at the start of the test.");

        roomDao.createRoom(new Room("Test Room 1", 5, 1));
        roomDao.createRoom(new Room("Test Room 2", 6, 2));

        List<Room> rooms = roomDao.getAllRooms();
        assertFalse(rooms.isEmpty(), "The list of rooms should not be empty.");
        assertEquals(2, rooms.size(), "There should be 2 rooms in the database.");
        System.out.println("testGetAllRooms PASSED.");
    }

    @Test
    void testUpdateRoom() throws SQLException, RoomNotFoundException {
        System.out.println("Running testUpdateRoom...");
        Room initialRoom = new Room("Original Room", 5, 1);
        roomDao.createRoom(initialRoom);

        Optional<Room> createdRoomOptional = roomDao.getAllRooms().stream().findFirst();
        assertTrue(createdRoomOptional.isPresent(), "The room should exist to be updated.");
        Room roomToUpdate = createdRoomOptional.get();

        roomToUpdate.setTheme("Room updated");
        roomToUpdate.setDifficultyLevel(9);
        roomToUpdate.setEscapeRoomId(2);

        assertDoesNotThrow(() -> roomDao.updateRoom(roomToUpdate),
                "Updating an existing room should not throw any exceptions.");

        Optional<Room> updatedRoomOptional = roomDao.getRoomById(roomToUpdate.getId());
        assertTrue(updatedRoomOptional.isPresent(), "The updated room should be able to be recovered.");
        Room updatedRoom = updatedRoomOptional.get();

        assertEquals("Room updated", updatedRoom.getTheme());
        assertEquals(9, updatedRoom.getDifficultyLevel());
        assertEquals(2, updatedRoom.getEscapeRoomId());
        System.out.println("testUpdateRoom PASSED.");
    }

    @Test
    void testUpdateRoomNotFound() {
        System.out.println("Running testUpdateRoomNotFound...");
        Room nonExistentRoom = new Room(999, "No Existe", 1, 1);

        assertThrows(RoomNotFoundException.class, () -> roomDao.updateRoom(nonExistentRoom),
                "Updating an existing room should throw any exceptions.");
        System.out.println("testUpdateRoomNotFound PASSED (threw RoomNotFoundException as expected).");
    }

    @Test
    void testDeleteRoom() throws SQLException, RoomNotFoundException {
        System.out.println("Running testDeleteRoom...");
        Room roomToDelete = new Room("Room a Borrar", 4, 1);
        roomDao.createRoom(roomToDelete);

        Optional<Room> createdRoomOptional = roomDao.getAllRooms().stream().findFirst();
        assertTrue(createdRoomOptional.isPresent(), "The room should exist to be deleted.");
        int idToDelete = createdRoomOptional.get().getId();

        assertDoesNotThrow(() -> roomDao.deleteRoom(idToDelete),
                "Deleting an existing room should not throw any exception.");

        Optional<Room> deletedRoom = roomDao.getRoomById(idToDelete);
        assertFalse(deletedRoom.isPresent(), "The room should be deleted.");
        System.out.println("testDeleteRoom PASSED.");
    }

    @Test
    void testDeleteRoomNotFound() {
        System.out.println("Running testDeleteRoomNotFound...");
        assertThrows(RoomNotFoundException.class, () -> roomDao.deleteRoom(999),
                "Deleting an existing room should throw any exception.");
        System.out.println("testDeleteRoomNotFound PASSED (threw RoomNotFoundException as expected).");
    }
}
