package dao;

import dbconnection.DBConnection;
import model.EscapeRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscapeRoomDAOImpl implements EscapeRoomDAO {

    @Override
    public void create(EscapeRoom room) {
        String sql = "INSERT INTO EscapeRoom (name, total_tickets) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getTotalTickets());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al crear EscapeRoom: " + e.getMessage());
        }
    }

    @Override
    public List<EscapeRoom> getAll() {
        List<EscapeRoom> rooms = new ArrayList<>();
        String sql = "SELECT * FROM EscapeRoom";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EscapeRoom room = new EscapeRoom();
                room.setId(rs.getInt("id"));
                room.setName(rs.getString("name"));
                room.setTotalTickets(rs.getInt("total_tickets"));
                rooms.add(room);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar EscapeRooms: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public void update(EscapeRoom room) {
        String sql = "UPDATE EscapeRoom SET name = ?, total_tickets = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getTotalTickets());
            stmt.setInt(3, room.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar EscapeRoom: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM EscapeRoom WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar EscapeRoom: " + e.getMessage());
        }
    }
}
