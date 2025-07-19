package dao;

import enums.Material;
import model.ClueObject;
import org.junit.jupiter.api.*;
import util.DatabaseConnection;
import util.EnvLoader;
import util.SQLExecutor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClueDAOImplementationTest {

    private ClueDao clueDao;

    @BeforeEach
    void setUp() throws SQLException {
        EnvLoader.getInstance();
        DatabaseConnection.getInstance();
        SQLExecutor sqlExecutor = new SQLExecutor();

        clueDao = new ClueDAOImplementation(sqlExecutor);

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM ClueObject");
            stmt.executeUpdate("DELETE FROM Room");
            stmt.executeUpdate("DELETE FROM EscapeRoom");

            stmt.executeUpdate("ALTER TABLE ClueObject AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE Room AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE EscapeRoom AUTO_INCREMENT = 1");

            stmt.executeUpdate("INSERT INTO EscapeRoom (name, total_tickets) VALUES ('Escape A', 10)");
            stmt.executeUpdate("INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES ('Test Room', 5, 1)");
        }
    }

    @Test
    void testCreateAndGetClueById() throws SQLException {
        ClueObject clue = new ClueObject("Ancient Scroll", new BigDecimal("19.99"), Material.PAPER, "Solve the riddle", false, 1);
        clueDao.createClue(clue);

        List<ClueObject> clues = clueDao.getAllClues();
        assertEquals(1, clues.size());

        ClueObject retrieved = clueDao.getClueById(clues.get(0).getId());
        assertNotNull(retrieved);
        assertEquals("Ancient Scroll", retrieved.getName());
        assertEquals(Material.PAPER, retrieved.getMaterial());
        assertFalse(retrieved.isSolved());
    }

    @Test
    void testGetAllClues() throws SQLException {
        ClueObject clue1 = new ClueObject("Key", new BigDecimal("5.00"), Material.METAL, "Find the door", false, 1);
        ClueObject clue2 = new ClueObject("Map", new BigDecimal("7.50"), Material.PAPER, "Locate the treasure", false, 1);
        clueDao.createClue(clue1);
        clueDao.createClue(clue2);

        List<ClueObject> clues = clueDao.getAllClues();
        assertEquals(2, clues.size());
    }

    @Test
    void testUpdateClue() throws SQLException {
        ClueObject clue = new ClueObject("Locked Box", new BigDecimal("12.00"), Material.METAL, "Find the code", false, 1);
        clueDao.createClue(clue);

        ClueObject created = clueDao.getAllClues().get(0);
        created.setName("Unlocked Box");
        created.setSolved(true);
        clueDao.updateClue(created);

        ClueObject updated = clueDao.getClueById(created.getId());
        assertEquals("Unlocked Box", updated.getName());
        assertTrue(updated.isSolved());
    }

    @Test
    void testDeleteClue() throws SQLException {
        ClueObject clue = new ClueObject("Fake Book", new BigDecimal("3.50"), Material.PLASTIC, "Open the hidden cover", false, 1);
        clueDao.createClue(clue);

        int id = clueDao.getAllClues().get(0).getId();
        clueDao.deleteClue(id);

        ClueObject deleted = clueDao.getClueById(id);
        assertNull(deleted);
    }

    @Test
    void testMarkClueAsSolved() throws SQLException {
        ClueObject clue = new ClueObject("Box with Numbers", new BigDecimal("15.00"), Material.METAL, "Solve the math puzzle", false, 1);
        clueDao.createClue(clue);

        int id = clueDao.getAllClues().get(0).getId();
        clueDao.markClueAsSolved(id);

        ClueObject solved = clueDao.getClueById(id);
        assertTrue(solved.isSolved());
    }

    @Test
    void testGetCluesByRoomId() throws SQLException {
        clueDao.createClue(new ClueObject("Torch", new BigDecimal("4.99"), Material.METAL, "Light up the way", false, 1));
        clueDao.createClue(new ClueObject("Rope", new BigDecimal("2.50"), Material.PAPER, "Climb down", false, 1));

        List<ClueObject> roomClues = clueDao.getCluesByRoomId(1);
        assertEquals(2, roomClues.size());
    }
}

