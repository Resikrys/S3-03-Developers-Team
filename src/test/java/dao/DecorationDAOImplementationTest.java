package dao;

import enums.Material;
import model.DecorationObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DatabaseConnection;
import util.EnvLoader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DecorationDAOImplementationTest {
    private DecorationObjectDAO decorationDao;

    @BeforeEach
    void setUp() throws SQLException {
        // Cargar variables de entorno y conexión
        EnvLoader.getInstance();
        DatabaseConnection.getInstance();

        decorationDao = new DecorationDAOImplementation();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            // Borra en orden correcto por integridad referencial
            stmt.executeUpdate("DELETE FROM ClueObject");
            stmt.executeUpdate("DELETE FROM DecorationObject");
            stmt.executeUpdate("DELETE FROM Room");
            stmt.executeUpdate("DELETE FROM EscapeRoom");

            // Resetea auto-increments
            stmt.executeUpdate("ALTER TABLE ClueObject AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE DecorationObject AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE Room AUTO_INCREMENT = 1");
            stmt.executeUpdate("ALTER TABLE EscapeRoom AUTO_INCREMENT = 1");

            // Crea datos base
            stmt.executeUpdate("INSERT INTO EscapeRoom (name, total_tickets) VALUES ('Escape Test', 20)");
            stmt.executeUpdate("INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES ('Test Room', 3, 1)");
        }


    }

    @Test
    void testCreateAndGetById() throws SQLException {
        DecorationObject obj = new DecorationObject("Vase", new BigDecimal("12.99"), Material.GLASS, 1);
        decorationDao.createDecoration(obj);

        List<DecorationObject> all = decorationDao.getAllDecorations();
        assertEquals(1, all.size());

        DecorationObject loaded = decorationDao.getDecorationById(all.get(0).getId());
        assertNotNull(loaded);
        assertEquals("Vase", loaded.getName());
    }

    @Test
    void testUpdateDecoration() throws SQLException {
        DecorationObject obj = new DecorationObject("Lamp", new BigDecimal("5.00"), Material.WOOD, 1);
        decorationDao.createDecoration(obj);

        DecorationObject created = decorationDao.getAllDecorations().get(0);
        created.setName("Updated Lamp");
        created.setPrice(new BigDecimal("7.50"));
        created.setMaterial(Material.METAL);
        created.setRoomId(1);

        decorationDao.updateDecoration(created);

        DecorationObject updated = decorationDao.getDecorationById(created.getId());
        assertEquals("Updated Lamp", updated.getName());
        assertEquals(new BigDecimal("7.50"), updated.getPrice());
        assertEquals(Material.METAL, updated.getMaterial());
    }

    @Test
    void testDeleteDecoration() throws SQLException {
        DecorationObject obj = new DecorationObject("Candle", new BigDecimal("2.00"), Material.PLASTIC, 1);
        decorationDao.createDecoration(obj);

        int id = decorationDao.getAllDecorations().get(0).getId();
        decorationDao.deleteDecoration(id);

        DecorationObject deleted = decorationDao.getDecorationById(id);
        assertNull(deleted);
    }

    @Test
    void testGetAllDecorations() throws SQLException {
        decorationDao.createDecoration(new DecorationObject("Chair", new BigDecimal("15.00"), Material.WOOD, 1));
        decorationDao.createDecoration(new DecorationObject("Table", new BigDecimal("25.00"), Material.METAL, 1));

        List<DecorationObject> decorations = decorationDao.getAllDecorations();
        assertEquals(2, decorations.size());
    }

    @Test
    void testGetByRoomId() throws SQLException {
        decorationDao.createDecoration(new DecorationObject("Mirror", new BigDecimal("10.00"), Material.GLASS, 1));
        decorationDao.createDecoration(new DecorationObject("Rug", new BigDecimal("8.00"), Material.PAPER, 1));

        List<DecorationObject> list = decorationDao.getDecorationsByRoomId(1);
        assertEquals(2, list.size());
    }
}

