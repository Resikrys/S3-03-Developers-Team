package manager;

import dao.DecorationDAOImplementation;
import dao.DecorationObjectDAO;
import enums.Material;
import exception.DecorationNotFoundException;
import model.DecorationObject;
import util.InputHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DecorationManager {
    private final DecorationObjectDAO decorationDao;
    private final InputHelper scanner;

    public DecorationManager(InputHelper inputHelper) {
        this.decorationDao = new DecorationDAOImplementation();
        this.scanner = inputHelper;
    }

    // Crea un nuevo objeto de decoración
    public void createDecoration() throws SQLException {
        String name = scanner.readString("Decoration name: ");
        BigDecimal price = scanner.readBigDecimal("Price: ");
        Material material = scanner.readEnum(Material.class, "Choose a material:");
        int roomId = scanner.readInt("Room ID: ");

        DecorationObject decoration = new DecorationObject(name, price, material, roomId);
        decorationDao.createDecoration(decoration);
        System.out.println("✅ Decoration object created successfully.");
    }

    // Busca un objeto por su ID
    public void getDecorationById() throws SQLException {
        int id = scanner.readInt("Enter the ID of the decoration: ");
        DecorationObject d = decorationDao.getDecorationById(id);
        if (d != null) {
            System.out.println("🎯 Found: " + d);
        } else {
            System.out.println("⚠️ No decoration found with ID " + id);
        }
    }

    // Lista todos los objetos de decoración
    public void listAllDecorations() throws SQLException {
        List<DecorationObject> list = decorationDao.getAllDecorations();
        if (list.isEmpty()) {
            System.out.println("📭 No decorations available.");
        } else {
            list.forEach(System.out::println);
        }
    }

    // Actualiza un objeto de decoración existente
    public void updateDecoration() throws SQLException, DecorationNotFoundException {
        int id = scanner.readInt("ID of the decoration to update: ");
        DecorationObject d = decorationDao.getDecorationById(id);
        if (d == null) {
            throw new DecorationNotFoundException("Decoration with ID " + id + " not found.");
        }

        String newName = scanner.readString("New name (current: " + d.getName() + "): ");
        BigDecimal newPrice = scanner.readBigDecimal("New price (current: " + d.getPrice() + "): ");
        String newMaterialStr = scanner.readString("New material (current: " + d.getMaterial() + "): ");
        Material newMaterial = Material.valueOf(newMaterialStr.toUpperCase());
        int newRoomId = scanner.readInt("New room ID (current: " + d.getRoomId() + "): ");

        d.setName(newName);
        d.setPrice(newPrice);
        d.setMaterial(newMaterial);
        d.setRoomId(newRoomId);

        decorationDao.updateDecoration(d);
        System.out.println("✅ Decoration updated successfully.");
    }

    // Elimina un objeto por ID
    public void deleteDecoration() throws SQLException, DecorationNotFoundException {
        int id = scanner.readInt("ID of the decoration to delete: ");
        decorationDao.deleteDecoration(id);
        System.out.println("🗑️ Decoration deleted successfully.");
    }

    // Muestra todas las decoraciones asociadas a un roomId concreto
    public void getByRoomId() throws SQLException {
        int roomId = scanner.readInt("Enter the Room ID: ");
        List<DecorationObject> list = decorationDao.getDecorationsByRoomId(roomId);
        if (list.isEmpty()) {
            System.out.println("❌ No decorations found in that room.");
        } else {
            list.forEach(System.out::println);
        }
    }
}
