package manager;

import dao.PlayerDAO;
import dao.PlayerDAOImpl;
import model.Player;
import util.InputHelper;

import java.util.List;

public class PlayerManager {
    private final PlayerDAO dao = new PlayerDAOImpl();

    public void createPlayer() {
        String name = InputHelper.readString("Enter player name: ");
        String email = InputHelper.readString("Enter email: ");
        boolean registered = InputHelper.readBoolean("Is registered?");
        int escapeRoomId = InputHelper.readInt("Enter EscapeRoom ID: ");

        dao.create(new Player(name, email, registered, escapeRoomId));
        System.out.println("✅ Player created!");
    }

    public void listPlayers() {
        List<Player> players = dao.getAll();
        if (players.isEmpty()) {
            System.out.println("⚠️ No players found.");
            return;
        }

        for (Player p : players) {
            System.out.printf("ID: %d | Name: %s | Email: %s | Registered: %b | EscapeRoom ID: %d%n",
                    p.getId(), p.getName(), p.getEmail(), p.isRegistered(), p.getEscapeRoomId());
        }
    }

    public void updatePlayer() {
        int id = InputHelper.readInt("Enter player ID to update: ");
        String name = InputHelper.readString("New name: ");
        String email = InputHelper.readString("New email: ");
        boolean registered = InputHelper.readBoolean("Is registered?");
        int escapeRoomId = InputHelper.readInt("EscapeRoom ID: ");

        dao.update(new Player(id, name, email, registered, escapeRoomId));
        System.out.println("🔁 Player updated.");
    }

    public void deletePlayer() {
        int id = InputHelper.readInt("Enter player ID to delete: ");
        dao.delete(id);
        System.out.println("🗑️ Player deleted.");
    }
}
