package manager;

import dao.PlayerDao;
import model.Player;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;

public class PlayerManager {

    private final PlayerDao playerDao;
    private final InputHelper scanner;

    public PlayerManager(PlayerDao playerDao, InputHelper inputHelper) {
        this.playerDao = playerDao;
        this.scanner = inputHelper;
    }

    public void createPlayer() throws SQLException {
        String name = scanner.readString("Enter player name: ");
        String email = scanner.readString("Enter player email: ");
        boolean registered = scanner.readBoolean("Is registered (true/false): ");
        int escapeRoomId = scanner.readInt("Enter escape room ID: ");

        Player player = new Player(name, email, registered, escapeRoomId);
        playerDao.createPlayer(player);
        System.out.println("✅ Player created successfully!");
    }

    public void listAllPlayers() throws SQLException {
        List<Player> players = playerDao.getAllPlayers();
        if (players.isEmpty()) {
            System.out.println("📭 No players found.");
        } else {
            players.forEach(System.out::println);
        }
    }

    public void getPlayerById() throws SQLException {
        int id = scanner.readInt("Enter player ID: ");
        Player player = playerDao.getPlayerById(id);
        if (player != null) {
            System.out.println("🎯 Found: " + player);
        } else {
            System.out.println("⚠️ Player not found.");
        }
    }

    public void updatePlayer() throws SQLException {
        int id = scanner.readInt("Enter ID of the player to update: ");
        Player existing = playerDao.getPlayerById(id);
        if (existing == null) {
            System.out.println("❌ No player with that ID.");
            return;
        }

        String name = scanner.readString("New name (" + existing.getName() + "): ");
        String email = scanner.readString("New email (" + existing.getEmail() + "): ");
        boolean registered = scanner.readBoolean("New registered status (" + existing.isRegistered() + "): ");
        int escapeRoomId = scanner.readInt("New escape room ID (" + existing.getEscapeRoomId() + "): ");

        Player updated = new Player(id, name, email, registered, escapeRoomId);
        playerDao.updatePlayer(updated);
        System.out.println("🔄 Player updated.");
    }

    public void deletePlayer() throws SQLException {
        int id = scanner.readInt("Enter ID of the player to delete: ");
        playerDao.deletePlayer(id);
        System.out.println("🗑️ Player deleted.");
    }
}
