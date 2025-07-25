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
    private final TicketManager ticketManager;

    public EscapeRoomManager(InputHelper inputhelper) {
        this.escapeRoomDao = new EscapeRoomDaoImpl(); // Instancia el DAO
        this.inputhelper = inputhelper;
        this.ticketManager = new TicketManager();
        // Inyecta el ScannerManager
    }

    public void createEscapeRoom() throws SQLException {
        String name = inputhelper.readString("Enter Escape Room name: ");
        int totalTickets = inputhelper.readInt("Enter total tickets: ");

        escapeRoomDao.createEscaperoom(new EscapeRoom(name, totalTickets));
        System.out.println("Escape Room '" + name + "' created successfully!");
    }

//    public void searchEscapeRoomById() throws SQLException {
//        int id = inputhelper.readInt("Enter ID of the Escape Room to search: ");
//        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);
//
//        if (optionalEscapeRoom.isPresent()) {
//            System.out.println("Escape Room found: " + optionalEscapeRoom.get());
//        } else {
//            System.out.println("Escape Room with ID " + id + " not found.");
//        }
//    }

    public void searchEscapeRoomById() throws SQLException {
        int id = inputhelper.readInt("Enter ID of the Escape Room to search: ");
        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);

        if (optionalEscapeRoom.isPresent()) {
            EscapeRoom foundEscapeRoom = optionalEscapeRoom.get();
            System.out.println("Escape Room found: " + foundEscapeRoom);
            // After finding an Escape Room, offer to view its tickets/revenue
            if (inputhelper.readBoolean("View tickets and revenue for this Escape Room? (yes/no): ")) {
                viewTicketsAndRevenueForEscapeRoom(foundEscapeRoom.getId(), foundEscapeRoom.getName());
            }
        } else {
            System.out.println("Escape Room with ID " + id + " not found.");
        }
    }

//    public void listAllEscapeRooms() throws SQLException {
//        List<EscapeRoom> escapeRooms = escapeRoomDao.getAll();
//        if (escapeRooms.isEmpty()) {
//            System.out.println("No Escape Rooms available.");
//        } else {
//            System.out.println("--- All Escape Rooms ---");
//            escapeRooms.forEach(System.out::println);
//        }
//    }

    public void listAllEscapeRooms() throws SQLException {
        List<EscapeRoom> escapeRooms = escapeRoomDao.getAll();
        if (escapeRooms.isEmpty()) {
            System.out.println("No Escape Rooms available.");
        } else {
            System.out.println("--- All Escape Rooms ---");
            escapeRooms.forEach(escapeRoom -> {
                System.out.print(escapeRoom);
                // Optionally, display revenue directly in the list
                double revenue = ticketManager.getTotalRevenueByEscapeRoomId(escapeRoom.getId());
                System.out.printf(" (Total Revenue: %.2f€)\n", revenue);
            });
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

    // --- NEW HELPER METHOD TO DISPLAY TICKETS AND REVENUE ---
    public void viewTicketsAndRevenueForEscapeRoom(int escapeRoomId, String escapeRoomName) {
        System.out.println("\n--- Tickets and Revenue for Escape Room: " + escapeRoomName + " (ID: " + escapeRoomId + ") ---");

        // List all tickets for this Escape Room
        List<model.Ticket> tickets = ticketManager.getTicketsByEscapeRoomId(escapeRoomId);
        if (tickets.isEmpty()) {
            System.out.println("📭 No tickets found for this Escape Room.");
        } else {
            System.out.println("--- Individual Tickets ---");
            tickets.forEach(System.out::println);
        }

        // Calculate and display total revenue for this Escape Room
        double totalRevenue = ticketManager.getTotalRevenueByEscapeRoomId(escapeRoomId);
        System.out.printf("💰 Total revenue for %s (ID: %d): %.2f€\n", escapeRoomName, escapeRoomId, totalRevenue);
    }
}
