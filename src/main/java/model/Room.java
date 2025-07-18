package model;

public class Room {
    private int id;
    private String theme;
    private int difficulty_level;

    public Room(int id, String theme, int difficulty_level) {
        this.id = id;
        this.theme = theme;
        this.difficulty_level = difficulty_level;
    }

    public Room(String theme, int difficulty_level) {
        this(0, theme, difficulty_level); // delega en el constructor completo
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getTheme() {return theme;}
    public void setTheme(String theme) {this.theme = theme;}
    public int getDifficultyLevel() {return difficulty_level;}
    public void setDifficultyLevel(int difficulty_level) {this.difficulty_level = difficulty_level;}

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", capacity=" + difficulty_level +
                '}';
    }
}
