// observer/NotificationService.java
package observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Subject {
    private static final NotificationService instance = new NotificationService();
    private final List<Observer> observers = new ArrayList<>();

    private NotificationService() {
    }

    public static NotificationService getInstance() {
        return instance;
    }

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) { // Prevent duplicates
            observers.add(observer);
            System.out.println("✅ Observer attached: " + observer.getPlayer().getName());
        } else {
            System.out.println("⚠️ Observer already attached: " + observer.getPlayer().getName());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("✅ Observer detached: " + observer.getPlayer().getName());
        } else {
            System.out.println("⚠️ Observer not found for detachment: " + observer.getPlayer().getName());
        }
    }

    @Override
    public void notifyObservers(NotificationEvent event) { // <--- CHANGE HERE
        System.out.println("\n--- NOTIFYING SUBSCRIBED PLAYERS ---");
        if (observers.isEmpty()) {
            System.out.println("No players subscribed to notifications.");
        }
        List<Observer> snapshot = new ArrayList<>(observers);
        for (Observer obs : snapshot) {
            obs.update(event); // Pass the NotificationEvent object
        }
    }

    @Override
    public List<Observer> getObservers() {
        return new ArrayList<>(observers);
    }
}