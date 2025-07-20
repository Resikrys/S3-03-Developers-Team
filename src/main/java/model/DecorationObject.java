package model;

import enums.Material;

import java.math.BigDecimal;

    public class DecorationObject {
        private int id;
        private String name;
        private BigDecimal price;
        private Material material;
        private Integer roomId;

        public DecorationObject(int id, String name, BigDecimal price, Material material, Integer roomId) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.material = material;
            this.roomId = roomId;
        }

        public DecorationObject(String name, BigDecimal price, Material material, Integer roomId) {
            this.name = name;
            this.price = price;
            this.material = material;
            this.roomId = roomId;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public Material getMaterial() { return material; }
        public void setMaterial(Material material) { this.material = material; }

        public Integer getRoomId() { return roomId; }
        public void setRoomId(Integer roomId) { this.roomId = roomId; }

        @Override
        public String toString() {
            return "DecorationObject{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", material=" + material +
                    ", roomId=" + roomId +
                    '}';
        }


}
