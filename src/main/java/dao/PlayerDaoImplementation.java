package dao;

import dbconnection.SQLExecutor;
import model.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoImplementation implements PlayerDao {
    private final SQLExecutor executor;

    public PlayerDaoImplementation(SQLExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void createPlayer(Player player) throws SQLException {
        String sql = "INSERT INTO Player (name, email, registered, escape_room_id) VALUES (?, ?, ?, ?)";
        executor.executeUpdate(sql,
                player.getName(),
                player.getEmail(),
                player.isRegistered(),
                player.getEscapeRoomId());
    }

    @Override
    public Player getPlayerById(int id) throws SQLException {
        String sql = "SELECT * FROM Player WHERE id = ?";
        return executor.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    return mapRowToPlayer(rs);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, id);
    }

    @Override
    public List<Player> getAllPlayers() throws SQLException {
        String sql = "SELECT * FROM Player";
        return executor.executeQuery(sql, rs -> {
            List<Player> list = new ArrayList<>();
            try {
                while (rs.next()) {
                    list.add(mapRowToPlayer(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return list;
        });
    }

    @Override
    public void updatePlayer(Player player) throws SQLException {
        String sql = "UPDATE Player SET name = ?, email = ?, registered = ?, escape_room_id = ? WHERE id = ?";
        executor.executeUpdate(sql,
                player.getName(),
                player.getEmail(),
                player.isRegistered(),
                player.getEscapeRoomId(),
                player.getId());
    }

    @Override
    public void deletePlayer(int id) throws SQLException {
        String sql = "DELETE FROM Player WHERE id = ?";
        executor.executeUpdate(sql, id);
    }

    private Player mapRowToPlayer(ResultSet rs) throws SQLException {
        return new Player(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getBoolean("registered"),
                rs.getInt("escape_room_id")
        );
    }
}