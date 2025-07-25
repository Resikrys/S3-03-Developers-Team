package dao;

import model.Player;

import java.sql.SQLException;
import java.util.List;

public interface PlayerDao {
    void createPlayer(Player player) throws SQLException;
    Player getPlayerById(int id) throws SQLException;
    List<Player> getAllPlayers() throws SQLException;
    void updatePlayer(Player player) throws SQLException;
    void deletePlayer(int id) throws SQLException;
}