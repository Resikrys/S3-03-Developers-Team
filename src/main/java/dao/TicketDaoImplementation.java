package dao;

import dbconnection.SQLExecutor;
import exception.TicketNotFoundException;
import model.Ticket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDaoImplementation implements TicketDAO {

    private final SQLExecutor sqlExecutor;

    public TicketDaoImplementation() {
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void create(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO Ticket (price, player_id, escape_room_id) VALUES (?, ?, ?)";
        try {
            sqlExecutor.executeUpdate(
                    sql,
                    ticket.getPrice(),
                    ticket.getPlayerId(),
                    ticket.getEscapeRoomId()
            );
        } catch (SQLException e) {
            System.err.println("❌ Error creating Ticket: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Ticket> getTicketById(int id) throws SQLException {
        String sql = "SELECT id, price, player_id, escape_room_id FROM Ticket WHERE id = ?";
        return sqlExecutor.executeQuery(
                sql,
                rs -> {
                    try {
                        if (rs.next()) {
                            return Optional.of(new Ticket(
                                    rs.getInt("id"),
                                    rs.getDouble("price"),
                                    rs.getInt("player_id"),
                                    rs.getInt("escape_room_id")
                            ));
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Error processing ResultSet for Ticket ID " + id + ": " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return Optional.empty();
                },
                id
        );
    }

    @Override
    public List<Ticket> getAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT id, price, player_id, escape_room_id FROM Ticket";

        sqlExecutor.executeQuery(
                sql,
                rs -> {
                    try {
                        while (rs.next()) {
                            tickets.add(new Ticket(
                                    rs.getInt("id"),
                                    rs.getDouble("price"),
                                    rs.getInt("player_id"),
                                    rs.getInt("escape_room_id")
                            ));
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Error processing ResultSet for Tickets: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return null;
                }
        );

        return tickets;
    }

    @Override
    public void update(Ticket ticket) throws SQLException, TicketNotFoundException {
        String sql = "UPDATE Ticket SET price = ?, player_id = ?, escape_room_id = ? WHERE id = ?";
        int rowsAffected = sqlExecutor.executeUpdate(
                sql,
                ticket.getPrice(),
                ticket.getPlayerId(),
                ticket.getEscapeRoomId(),
                ticket.getId()
        );

        if (rowsAffected == 0) {
            throw new TicketNotFoundException("Ticket with ID " + ticket.getId() + " not found for update.");
        }
    }

    @Override
    public void delete(int id) throws SQLException, TicketNotFoundException {
        String sql = "DELETE FROM Ticket WHERE id = ?";
        int rowsAffected = sqlExecutor.executeUpdate(sql, id);

        if (rowsAffected == 0) {
            throw new TicketNotFoundException("Ticket with ID " + id + " not found for deletion.");
        }
    }
}