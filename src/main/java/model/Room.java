package model;

public class Room {
    private int id;
    private String theme;
    private int difficultyLevel;
    private Integer escapeRoomId;

    public Room(int id, String theme, int difficultyLevel, Integer escapeRoomId) {
        this.id = id;
        this.theme = theme;
        this.difficultyLevel = difficultyLevel;
        this.escapeRoomId = escapeRoomId;
    }

    public Room(String theme, int difficultyLevel, Integer escapeRoomId) {
        this.theme = theme;
        this.difficultyLevel = difficultyLevel;
        this.escapeRoomId = escapeRoomId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Integer getEscapeRoomId() { return escapeRoomId; }
    public void setEscapeRoomId(Integer escapeRoomId) { this.escapeRoomId = escapeRoomId; }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", escapeRoomId=" + escapeRoomId +
                '}';
    }
}
