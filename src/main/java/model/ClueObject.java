package model;

import enums.Material;

import java.math.BigDecimal;

public class ClueObject {
    private int id;
    private String name;
    private BigDecimal price;
    private Material material;
    private String puzzleDescription;
    private boolean solved;
    private int roomId;

    public ClueObject(int id, String name, BigDecimal price, Material material, String puzzleDescription, boolean solved, int roomId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.material = material;
        this.puzzleDescription = puzzleDescription;
        this.solved = solved;
        this.roomId = roomId;
    }

    public ClueObject(String name, BigDecimal price, Material material, String puzzleDescription, boolean solved, int roomId) {
        this.name = name;
        this.price = price;
        this.material = material;
        this.puzzleDescription = puzzleDescription;
        this.solved = solved;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Material getMaterial() {
        return material;
    }

    public String getPuzzleDescription() {
        return puzzleDescription;
    }

    public boolean isSolved() {
        return solved;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setPuzzleDescription(String puzzleDescription) {
        this.puzzleDescription = puzzleDescription;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Clue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", material=" + material +
                ", puzzleDescription='" + puzzleDescription + '\'' +
                ", solved=" + solved +
                ", rewardId=" + /*rewardId + */
                ", roomId=" + roomId +
                '}';
    }
}
