package dao;

import dbconnection.SQLExecutor;
import exception.TicketNotFoundException;
import model.Ticket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDaoImplementation implements TicketDAO {

    private SQLExecutor sqlExecutor;

    public TicketDaoImplementation() {
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void createTicket(Ticket ticket) throws SQLException {
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
                        System.err.println("❌ Error reading Ticket by ID: " + e.getMessage());
                        e.printStackTrace();
                    }
                    return Optional.empty();
                },
                id
        );
    }

    @Override
    public List<Ticket> getAllTickets() throws SQLException {
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
                        System.err.println("❌ Error processing all Tickets: " + e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                }
        );
        return tickets;
    }

    @Override
    public void updateTicket(Ticket ticket) throws SQLException, TicketNotFoundException {
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
    public void deleteTicket(int id) throws SQLException, TicketNotFoundException {
        String sql = "DELETE FROM Ticket WHERE id = ?";
        int rowsAffected = sqlExecutor.executeUpdate(sql, id);
        if (rowsAffected == 0) {
            throw new TicketNotFoundException("Ticket with ID " + id + " not found for deletion.");
        }
    }

    @Override
    public int countTicketsByPlayerId(int playerId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Ticket WHERE player_id = ?";
        return sqlExecutor.executeQuery(
                sql,
                rs -> {
                    try {
                        return rs.next() ? rs.getInt("count") : 0;
                    } catch (SQLException e) {
                        System.err.println("❌ Error counting tickets by player ID: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                },
                playerId
        );
    }

    @Override
    public int countTicketsByEscapeRoomId(int escapeRoomId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Ticket WHERE escape_room_id = ?";
        return sqlExecutor.executeQuery(
                sql,
                rs -> {
                    try {
                        return rs.next() ? rs.getInt("count") : 0;
                    } catch (SQLException e) {
                        System.err.println("❌ Error counting tickets by escape room ID: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                },
                escapeRoomId
        );
    }

    @Override
    public List<Ticket> getTicketsByEscapeRoomId(int escapeRoomId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT id, price, player_id, escape_room_id FROM Ticket WHERE escape_room_id = ?";

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
                        System.err.println("❌ Error processing tickets by escape room ID: " + e.getMessage());
                        e.printStackTrace();
                    }
                    return null;
                },
                escapeRoomId
        );
        return tickets;
    }

    @Override
    public double getTotalRevenueByEscapeRoomId(int escapeRoomId) throws SQLException {
        String sql = "SELECT SUM(price) AS total_revenue FROM Ticket WHERE escape_room_id = ?";
        return sqlExecutor.executeQuery(
                sql,
                rs -> {
                    try {
                        if (rs.next()) {
                            double total = rs.getDouble("total_revenue");
                            if (rs.wasNull()) {
                                return 0.0;
                            }
                            return total;
                        }
                    } catch (SQLException e) {
                        System.err.println("❌ Error calculating total revenue by escape room ID: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    return 0.0;
                },
                escapeRoomId
        );
    }
}