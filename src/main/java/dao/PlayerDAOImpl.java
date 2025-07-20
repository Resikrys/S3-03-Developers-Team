package dao;

import dbconnection.DBConnection;
import model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAOImpl implements PlayerDAO {

    @Override
    public void create(Player player) {
        String sql = "INSERT INTO Player (name, email, registered, escape_room_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getEmail());
            stmt.setBoolean(3, player.isRegistered());
            stmt.setInt(4, player.getEscapeRoomId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Player";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Player p = new Player();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setEmail(rs.getString("email"));
                p.setRegistered(rs.getBoolean("registered"));
                p.setEscapeRoomId(rs.getInt("escape_room_id"));
                players.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public void update(Player player) {
        String sql = "UPDATE Player SET name=?, email=?, registered=?, escape_room_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getEmail());
            stmt.setBoolean(3, player.isRegistered());
            stmt.setInt(4, player.getEscapeRoomId());
            stmt.setInt(5, player.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Player WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
