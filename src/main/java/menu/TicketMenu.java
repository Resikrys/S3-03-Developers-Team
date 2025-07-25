package menu;

import manager.TicketManager; // Importa tu TicketManager
import model.Ticket; // Importa tu modelo Ticket
import util.InputHelper; // Tu clase de utilidad para la entrada de usuario

import java.sql.SQLException; // Para manejar excepciones de SQL
import java.util.List;
import java.util.Optional;

public class TicketMenu {
    private final TicketManager ticketManager;
    private final InputHelper inputHelper;

    public TicketMenu(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
        this.ticketManager = new TicketManager(); // Instancia tu TicketManager
    }

    public void showMenu() {
        int option;
        do {
            printMenu();
            option = inputHelper.readInt("Choose an option: ");

            try {
                handleOption(option);
            } catch (SQLException e) { // Captura SQLException desde el manager
                System.err.println("❌ An SQL error occurred during the Ticket operation: " + e.getMessage());
                // e.printStackTrace(); // Descomentar para depuración
            } catch (Exception e) { // Captura otras excepciones
                System.err.println("❌ An unexpected error occurred during the Ticket operation: " + e.getMessage());
                // e.printStackTrace(); // Descomentar para depuración
            }
        } while (option != 0);
    }

    private void printMenu() {
        System.out.println("\n--- Ticket Management ---");
        System.out.println("1. Create Ticket");
        System.out.println("2. Search Ticket by ID");
        System.out.println("3. List All Tickets");
        System.out.println("4. Update Ticket");
        System.out.println("5. Delete Ticket");
        System.out.println("6. Count Tickets by Player ID");
        System.out.println("7. Count Tickets by Escape Room ID");
        System.out.println("8. List Tickets by Escape Room ID");
        System.out.println("9. Calculate Total Revenue for an Escape Room");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
    }

    private void handleOption(int option) throws SQLException { // Lanza SQLException para que el showMenu la capture
        switch (option) {
            case 1 -> createTicket();
            case 2 -> getTicketById();
            case 3 -> getAllTickets();
            case 4 -> updateTicket();
            case 5 -> deleteTicket();
            case 6 -> countTicketsByPlayerId();
            case 7 -> countTicketsByEscapeRoomId();
            case 8 -> listTicketsByEscapeRoomId();
            case 9 -> calculateRevenueForEscapeRoom();
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Invalid option.");
        }
    }

    private void createTicket() {
        System.out.println("\n--- Create New Ticket ---");
        double price = inputHelper.readDouble("Enter ticket price: ");
        int playerId = inputHelper.readInt("Enter player ID: ");
        int escapeRoomId = inputHelper.readInt("Enter escape room ID: ");

        // Crea una instancia de Ticket. El ID se generará automáticamente en la DB
        // NO incluyas 'id' en el constructor si la DB lo auto-incrementa.
        Ticket newTicket = new Ticket(price, playerId, escapeRoomId);
        ticketManager.createTicket(newTicket);
        System.out.println("✅ Ticket creation process initiated."); // El Manager o DAO debería confirmar éxito/error
    }

    private void getTicketById() {
        System.out.println("\n--- Search Ticket by ID ---");
        int id = inputHelper.readInt("Enter Ticket ID: ");
        Optional<Ticket> ticket = ticketManager.getTicketById(id);
        if (ticket.isPresent()) {
            System.out.println("🎯 Found Ticket: " + ticket.get());
        } else {
            System.out.println("⚠️ No ticket found with ID: " + id);
        }
    }

    private void getAllTickets() {
        System.out.println("\n--- All Tickets ---");
        List<Ticket> tickets = ticketManager.getAllTickets();
        if (tickets.isEmpty()) {
            System.out.println("📭 No tickets available.");
        } else {
            tickets.forEach(System.out::println);
        }
    }

    private void updateTicket() {
        System.out.println("\n--- Update Ticket ---");
        int id = inputHelper.readInt("Enter ID of the ticket to update: ");
        Optional<Ticket> existingTicketOptional = ticketManager.getTicketById(id);

        if (existingTicketOptional.isPresent()) {
            Ticket existingTicket = existingTicketOptional.get();
            System.out.println("Current Ticket details: " + existingTicket);

            double newPrice = inputHelper.readDouble("New price (current: " + existingTicket.getPrice() + "): ");
            int newPlayerId = inputHelper.readInt("New player ID (current: " + existingTicket.getPlayerId() + "): ");
            int newEscapeRoomId = inputHelper.readInt("New escape room ID (current: " + existingTicket.getEscapeRoomId() + "): ");

            existingTicket.setPrice(newPrice);
            existingTicket.setPlayerId(newPlayerId);
            existingTicket.setEscapeRoomId(newEscapeRoomId);

            ticketManager.updateTicket(existingTicket);
            System.out.println("✅ Ticket update process initiated.");
        } else {
            System.out.println("⚠️ No ticket found with ID: " + id);
        }
    }

    private void deleteTicket() {
        System.out.println("\n--- Delete Ticket ---");
        int id = inputHelper.readInt("Enter ID of the ticket to delete: ");
        ticketManager.deleteTicket(id);
        System.out.println("✅ Ticket deletion process initiated.");
    }

    private void countTicketsByPlayerId() {
        System.out.println("\n--- Count Tickets by Player ID ---");
        int playerId = inputHelper.readInt("Enter Player ID: ");
        int count = ticketManager.countTicketsByPlayerId(playerId);
        System.out.println("📊 Player " + playerId + " has " + count + " tickets.");
    }

    private void countTicketsByEscapeRoomId() {
        System.out.println("\n--- Count Tickets by Escape Room ID ---");
        int escapeRoomId = inputHelper.readInt("Enter Escape Room ID: ");
        int count = ticketManager.countTicketsByEscapeRoomId(escapeRoomId);
        System.out.println("📊 Escape Room " + escapeRoomId + " has " + count + " tickets.");
    }

    private void listTicketsByEscapeRoomId() { // <-- NEW METHOD
        System.out.println("\n--- List Tickets by Escape Room ID ---");
        int escapeRoomId = inputHelper.readInt("Enter Escape Room ID: ");
        List<Ticket> tickets = ticketManager.getTicketsByEscapeRoomId(escapeRoomId);
        if (tickets.isEmpty()) {
            System.out.println("📭 No tickets found for Escape Room ID: " + escapeRoomId);
        } else {
            System.out.println("--- Tickets for Escape Room ID: " + escapeRoomId + " ---");
            tickets.forEach(System.out::println);
        }
    }

    private void calculateRevenueForEscapeRoom() { // <-- NEW METHOD
        System.out.println("\n--- Calculate Total Revenue for an Escape Room ---");
        int escapeRoomId = inputHelper.readInt("Enter Escape Room ID: ");
        double totalRevenue = ticketManager.getTotalRevenueByEscapeRoomId(escapeRoomId);
        System.out.printf("💰 Total revenue for Escape Room ID %d: %.2f€\n", escapeRoomId, totalRevenue);
    }
}
