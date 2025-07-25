package model;

public class EscapeRoom {
    private int id;
    private String name;
    private int totalTickets;

    public EscapeRoom() {}

    public EscapeRoom(String name, int totalTickets) {
        this.name = name;
        this.totalTickets = totalTickets;
    }

    public EscapeRoom(int id, String name, int totalTickets) {
        this.id = id;
        this.name = name;
        this.totalTickets = totalTickets;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    @Override
    public String toString() {
        return "---------------------EscapeRoom---------------------- \n{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalTickets=" + totalTickets +
                '}';
    }
}
