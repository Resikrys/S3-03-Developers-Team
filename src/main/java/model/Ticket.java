package model;

public class Ticket {
    private int id;
    private double price;
    private int playerId;
    private int escapeRoomId;

    public Ticket(int id, double price, int playerId, int escapeRoomId) {
        this.id = id;
        this.price = price;
        this.playerId = playerId;
        this.escapeRoomId = escapeRoomId;
    }


    public Ticket(double price, int playerId, int escapeRoomId) {
        this.price = price;
        this.playerId = playerId;
        this.escapeRoomId = escapeRoomId;
    }

    public Ticket() {}

    // Getters y Setters

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getEscapeRoomId() {
        return escapeRoomId;
    }

    public void setEscapeRoomId(int escapeRoomId) {
        this.escapeRoomId = escapeRoomId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", price=" + price +
                ", playerId=" + playerId +
                ", escapeRoomId=" + escapeRoomId +
                '}';
    }
}
