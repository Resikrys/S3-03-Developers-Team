package manager;

import dao.PlayerDao;
import dao.PlayerDaoImplementation;
import dbconnection.SQLExecutor;
import observer.NotificationEventSource;
import observer.Observer;
import model.Player;

import java.sql.SQLException;
import java.util.List;

public class NotificationManager {
    private final PlayerDao playerDao;
    private final NotificationEventSource eventSource;

    public NotificationManager() {
        this.playerDao = new PlayerDaoImplementation(new SQLExecutor());
        this.eventSource = new NotificationEventSource();
        registerObservers();
    }

    private void registerObservers() {
        try {
            List<Player> allPlayers = playerDao.getAllPlayers();
            for (Player p : allPlayers) {
                if (p.isRegistered()) {
                    Observer observer = message ->
                            System.out.println("📣 Notification sent to Player ID " + p.getId() + ": " + message);
                    eventSource.addObserver(observer);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Could not load players for notification system: " + e.getMessage());
        }
    }

    public void notifyAll(String message) {
        eventSource.notifyObservers(message);
    }
}