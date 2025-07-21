package manager;

import dao.EscapeRoomDAO;
import dao.EscapeRoomDaoImpl;
import exception.EscapeRoomNotFoundException;
import exception.InvalidInputException;
import model.EscapeRoom;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EscapeRoomManager {
    private final EscapeRoomDAO escapeRoomDao;
    private final InputHelper inputhelper;

    public EscapeRoomManager(InputHelper inputhelper) {
        this.escapeRoomDao = new EscapeRoomDaoImpl(); // Instancia el DAO
        this.inputhelper = inputhelper;          // Inyecta el ScannerManager
    }

    public void createEscapeRoom() throws SQLException {
        String name = inputhelper.readString("Enter Escape Room name: ");
        int totalTickets = inputhelper.readInt("Enter total tickets: ");

        escapeRoomDao.createEscaperoom(new EscapeRoom(name, totalTickets));
        System.out.println("Escape Room '" + name + "' created successfully!");
    }

    public void searchEscapeRoomById() throws SQLException {
        int id = inputhelper.readInt("Enter ID of the Escape Room to search: ");
        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);

        if (optionalEscapeRoom.isPresent()) {
            System.out.println("Escape Room found: " + optionalEscapeRoom.get());
        } else {
            System.out.println("Escape Room with ID " + id + " not found.");
        }
    }

    public void listAllEscapeRooms() throws SQLException {
        List<EscapeRoom> escapeRooms = escapeRoomDao.getAll();
        if (escapeRooms.isEmpty()) {
            System.out.println("No Escape Rooms available.");
        } else {
            System.out.println("--- All Escape Rooms ---");
            escapeRooms.forEach(System.out::println);
        }
    }

    public void updateEscapeRoom() throws SQLException, EscapeRoomNotFoundException {
        int id = inputhelper.readInt("Enter ID of the Escape Room to update: ");
        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);

        if (optionalEscapeRoom.isPresent()) {
            EscapeRoom escapeRoomToUpdate = optionalEscapeRoom.get();
            System.out.println("Current details: " + escapeRoomToUpdate);

            String newName = inputhelper.readString("Enter new name (current: " + escapeRoomToUpdate.getName() + "): ");
            int newTotalTickets = inputhelper.readInt("Enter new total tickets (current: " + escapeRoomToUpdate.getTotalTickets() + "): ");

            escapeRoomToUpdate.setName(newName);
            escapeRoomToUpdate.setTotalTickets(newTotalTickets);

            escapeRoomDao.updateEscaperoom(escapeRoomToUpdate); // Puede lanzar EscapeRoomNotFoundException
            System.out.println("Escape Room with ID " + id + " updated successfully!");
        } else {
            System.out.println("Escape Room with ID " + id + " not found for update.");
        }
    }

    public void deleteEscapeRoom() throws SQLException, EscapeRoomNotFoundException {
        int id = inputhelper.readInt("Enter ID of the Escape Room to delete: ");
        escapeRoomDao.deleteEscaperoom(id); // Puede lanzar EscapeRoomNotFoundException
        System.out.println("Escape Room with ID " + id + " deleted successfully!");
    }
}
