package dao;

import exception.TicketNotFoundException;
import model.Ticket;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TicketDAO {

    void create(Ticket ticket) throws SQLException;

    Optional<Ticket> getTicketById(int id) throws SQLException;

    List<Ticket> getAll() throws SQLException;

    void update(Ticket ticket) throws SQLException, TicketNotFoundException;

    void delete(int id) throws SQLException, TicketNotFoundException;
}