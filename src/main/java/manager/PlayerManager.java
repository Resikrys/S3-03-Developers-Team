package manager;

import dao.PlayerDao;
import model.Player;
import observer.NotificationService;
import observer.UserObserver;
import util.InputHelper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private final PlayerDao playerDao;
    private final InputHelper scanner;
    private final Map<Integer, UserObserver> activeObservers = new HashMap<>(); // To keep track of UserObservers by Player ID

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
//            players.forEach(System.out::println);
            System.out.println("--- All Players ---");
            players.forEach(p -> {
                System.out.print(p);
                if (activeObservers.containsKey(p.getId())) {
                    System.out.println(" (Subscribed: YES)");
                } else {
                    System.out.println(" (Subscribed: NO)");
                }
            });
        }
    }

    public void getPlayerById() throws SQLException {
        int id = scanner.readInt("Enter player ID: ");
        Player player = playerDao.getPlayerById(id);
        if (player != null) {
            System.out.println("🎯 Found: " + player);
            if (activeObservers.containsKey(player.getId())) {
                System.out.println("   Subscribed to notifications: YES");
                System.out.println("   Notifications received:");
                player.getNotifications().forEach(n -> System.out.println("     - " + n));
            } else {
                System.out.println("   Subscribed to notifications: NO");
            }
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
//        Integer escapeRoomId = inputHelper.readOptionalInt("New escape room ID (" +
//                (existing.getEscapeRoomId() != null ? existing.getEscapeRoomId() : "NULL") + ", leave blank if not applicable): ");

        existing.setName(name);
        existing.setEmail(email);
        existing.setRegistered(registered);
        existing.setEscapeRoomId(escapeRoomId);
//        Player updated = new Player(id, name, email, registered, escapeRoomId);
        playerDao.updatePlayer(existing);
        System.out.println("🔄 Player updated.");
    }

    public void deletePlayer() throws SQLException {
        int id = scanner.readInt("Enter ID of the player to delete: ");
//        playerDao.deletePlayer(id);
//        System.out.println("🗑️ Player deleted.");
        Player playerToDelete = playerDao.getPlayerById(id);
        if (playerToDelete != null) {
            // Detach UserObserver before deleting the player from DB
            if (activeObservers.containsKey(playerToDelete.getId())) {
                UserObserver obs = activeObservers.get(playerToDelete.getId());
                NotificationService.getInstance().detach(obs); // Detach from service
                activeObservers.remove(playerToDelete.getId()); // Remove from map
            }
            playerDao.deletePlayer(id);
            System.out.println("🗑️ Player deleted.");
        } else {
            System.out.println("❌ Player not found for deletion.");
        }
    }

    // --- NEW METHODS FOR SUBSCRIPTION ---
    public void subscribePlayerToNotifications() throws SQLException {
        int id = scanner.readInt("Enter Player ID to subscribe to notifications: ");
        Player player = playerDao.getPlayerById(id);
        if (player != null) {
            if (!activeObservers.containsKey(player.getId())) {
                UserObserver observer = new UserObserver(player);
                NotificationService.getInstance().attach(observer);
                activeObservers.put(player.getId(), observer); // Store the observer
                System.out.println("✅ Player " + player.getName() + " subscribed to notifications.");
            } else {
                System.out.println("⚠️ Player " + player.getName() + " is already subscribed.");
            }
        } else {
            System.out.println("❌ Player not found with ID: " + id);
        }
    }

    public void unsubscribePlayerFromNotifications() throws SQLException {
        int id = scanner.readInt("Enter Player ID to unsubscribe from notifications: ");
        Player player = playerDao.getPlayerById(id); // Fetch player for name/info
        if (player != null) {
            if (activeObservers.containsKey(player.getId())) {
                UserObserver observer = activeObservers.get(player.getId());
                NotificationService.getInstance().detach(observer);
                activeObservers.remove(player.getId());
                System.out.println("✅ Player " + player.getName() + " unsubscribed from notifications.");
            } else {
                System.out.println("⚠️ Player " + player.getName() + " is not currently subscribed.");
            }
        } else {
            System.out.println("❌ Player not found with ID: " + id);
        }
    }

    public void listSubscribedPlayers() {
        if (activeObservers.isEmpty()) {
            System.out.println("📭 No players currently subscribed to notifications.");
        } else {
            System.out.println("--- Subscribed Players ---");
            activeObservers.values().forEach(userObs ->
                    System.out.println("ID: " + userObs.getId() + ", Name: " + userObs.getPlayer().getName())
            );
        }
    }
}