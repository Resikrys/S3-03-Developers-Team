package manager;


import dao.TicketDAO;
import dao.TicketDaoImplementation;
import exception.TicketNotFoundException;
import model.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TicketManager {

    private TicketDAO ticketDAO;

    public TicketManager() {
        this.ticketDAO = new TicketDaoImplementation();
    }

    public void createTicket(Ticket ticket) {
        try {
            ticketDAO.createTicket(ticket);
        } catch (SQLException e) {
            System.err.println("❌ Error creating ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Optional<Ticket> getTicketById(int id) {
        try {
            return ticketDAO.getTicketById(id);
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving ticket by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Ticket> getAllTickets() {
        try {
            return ticketDAO.getAllTickets();
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving all tickets: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateTicket(Ticket ticket) {
        try {
            ticketDAO.updateTicket(ticket);
        } catch (TicketNotFoundException e) {
            System.err.println("⚠️ Ticket not found for update: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error updating ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteTicket(int id) {
        try {
            ticketDAO.deleteTicket(id);
        } catch (TicketNotFoundException e) {
            System.err.println("⚠️ Ticket not found for deletion: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Error deleting ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int countTicketsByPlayerId(int playerId) {
        try {
            return ticketDAO.countTicketsByPlayerId(playerId);
        } catch (SQLException e) {
            System.err.println("❌ Error counting tickets by player ID: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public int countTicketsByEscapeRoomId(int escapeRoomId) {
        try {
            return ticketDAO.countTicketsByEscapeRoomId(escapeRoomId);
        } catch (SQLException e) {
            System.err.println("❌ Error counting tickets by escape room ID: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
