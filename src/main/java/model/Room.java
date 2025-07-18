package model;

public class Room {
    private int id;
    private String theme;
    private int difficultyLevel;
    private Integer escapeRoomId; // Usamos Integer para permitir valores NULL

    // Constructor con ID (para recuperar de DB)
    public Room(int id, String theme, int difficultyLevel, Integer escapeRoomId) {
        this.id = id;
        this.theme = theme;
        this.difficultyLevel = difficultyLevel;
        this.escapeRoomId = escapeRoomId;
    }

    // Constructor sin ID (para crear nuevas Rooms, el ID será auto-generado)
    public Room(String theme, int difficultyLevel, Integer escapeRoomId) {
        this.theme = theme;
        this.difficultyLevel = difficultyLevel;
        this.escapeRoomId = escapeRoomId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public int getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(int difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Integer getEscapeRoomId() { return escapeRoomId; } // Retorna Integer
    public void setEscapeRoomId(Integer escapeRoomId) { this.escapeRoomId = escapeRoomId; } // Recibe Integer

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
