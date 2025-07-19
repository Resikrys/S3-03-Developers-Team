package manager;

import dao.EscapeRoomDAO;
import dao.EscapeRoomDAOImpl;
import model.EscapeRoom;
import util.InputHelper;

import java.util.List;

public class EscapeRoomManager {
    private final EscapeRoomDAO dao = new EscapeRoomDAOImpl();

    public void createEscapeRoom() {
        String name = InputHelper.readString("Enter name: ");
        int totalTickets = InputHelper.readInt("Enter total tickets: ");

        dao.create(new EscapeRoom(name, totalTickets));
        System.out.println("✅ EscapeRoom created!");
    }

    public void listEscapeRooms() {
        List<EscapeRoom> rooms = dao.getAll();
        if (rooms.isEmpty()) {
            System.out.println("⚠️ No EscapeRooms found.");
            return;
        }
        for (EscapeRoom room : rooms) {
            System.out.printf("ID: %d | Name: %s | Tickets: %d%n", room.getId(), room.getName(), room.getTotalTickets());
        }
    }

    public void updateEscapeRoom() {
        int id = InputHelper.readInt("Enter ID to update: ");
        String name = InputHelper.readString("New name: ");
        int totalTickets = InputHelper.readInt("New total tickets: ");

        EscapeRoom room = new EscapeRoom(id, name, totalTickets);
        dao.update(room);
        System.out.println("🔁 EscapeRoom updated.");
    }

    public void deleteEscapeRoom() {
        int id = InputHelper.readInt("Enter ID to delete: ");
        dao.delete(id);
        System.out.println("🗑️ EscapeRoom deleted.");
    }
}
