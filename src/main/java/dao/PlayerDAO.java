package dao;

import model.Player;

import java.util.List;

public interface PlayerDAO {
    void create(Player player);
    List<Player> getAll();
    void update(Player player);
    void delete(int id);
}