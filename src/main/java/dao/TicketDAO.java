package dao;

import exception.TicketNotFoundException;
import model.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    void createTicket(Ticket ticket) throws SQLException;

    Optional<Ticket> getTicketById(int id) throws SQLException;

    List<Ticket> getAllTickets() throws SQLException;

    void updateTicket(Ticket ticket) throws SQLException;

    void deleteTicket(int id) throws SQLException;

    int countTicketsByPlayerId(int playerId) throws SQLException;

    int countTicketsByEscapeRoomId(int escapeRoomId) throws SQLException;
}