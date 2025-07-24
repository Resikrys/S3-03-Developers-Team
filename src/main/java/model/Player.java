package model;

import observer.Observer;
import observer.NotificationEvent;
import observer.EventType;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String name;
    private String email;
    private boolean registered;
    private int escapeRoomId;
    //private Integer escapeRoomId;
    private final List<String> notifications = new ArrayList<>();

    public Player() {}

    public Player(String name, String email, boolean registered, int escapeRoomId) {
        this.name = name;
        this.email = email;
        this.registered = registered;
        this.escapeRoomId = escapeRoomId;
    }

    public Player(int id, String name, String email, boolean registered, int escapeRoomId) {
        this(name, email, registered, escapeRoomId);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isRegistered() { return registered; }
    public void setRegistered(boolean registered) { this.registered = registered; }

    public int getEscapeRoomId() { return escapeRoomId; }
    public void setEscapeRoomId(int escapeRoomId) { this.escapeRoomId = escapeRoomId; }

    public void addNotification(String notification) { // <-- NEW METHOD
        this.notifications.add(notification);
    }

    public List<String> getNotifications() { // <-- NEW METHOD (for displaying)
        return new ArrayList<>(notifications);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", registered=" + registered +
                ", escapeRoomId=" + escapeRoomId +
                '}';
    }
}
