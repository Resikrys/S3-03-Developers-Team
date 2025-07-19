package test;

import dao.EscapeRoomDAO;
import dao.EscapeRoomDAOImpl;
import model.EscapeRoom;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EscapeRoomDAOTest {

    private static final EscapeRoomDAO dao = new EscapeRoomDAOImpl();
    private static int testId;

    @Test
    @Order(1)
    void testCreateEscapeRoom() {
        EscapeRoom room = new EscapeRoom("Test Room", 10);
        dao.create(room);

        List<EscapeRoom> rooms = dao.getAll();
        assertFalse(rooms.isEmpty(), "La lista no debería estar vacía después del insert");

        EscapeRoom last = rooms.get(rooms.size() - 1);
        assertEquals("Test Room", last.getName());
        assertEquals(10, last.getTotalTickets());

        testId = last.getId(); // Guardamos ID para próximas pruebas
    }

    @Test
    @Order(2)
    void testUpdateEscapeRoom() {
        EscapeRoom updated = new EscapeRoom(testId, "Updated Room", 15);
        dao.update(updated);

        List<EscapeRoom> rooms = dao.getAll();
        EscapeRoom result = rooms.stream()
                .filter(r -> r.getId() == testId)
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals("Updated Room", result.getName());
        assertEquals(15, result.getTotalTickets());
    }

    @Test
    @Order(3)
    void testDeleteEscapeRoom() {
        dao.delete(testId);

        List<EscapeRoom> rooms = dao.getAll();
        boolean exists = rooms.stream().anyMatch(r -> r.getId() == testId);

        assertFalse(exists, "El EscapeRoom debería haber sido eliminado");
    }
}
