package manager;

import dao.DecorationDAOImplementation;
import dao.DecorationObjectDAO;
import enums.Material;
import exception.DecorationNotFoundException;
import model.DecorationObject;
import util.ScannerManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DecorationManager {
    private final DecorationObjectDAO decorationDao;
    private final ScannerManager scanner;

    public DecorationManager(ScannerManager scannerManager) {
        this.decorationDao = new DecorationDAOImplementation();
        this.scanner = scannerManager;
    }

    public void handleDecorationCrud() {
        int option;
        do {
            System.out.println("\n--- Decoration Objects CRUD ---");
            System.out.println("1. Create Decoration");
            System.out.println("2. Search Decoration by ID");
            System.out.println("3. List All Decorations");
            System.out.println("4. Update Decoration");
            System.out.println("5. Delete Decoration");
            System.out.println("6. Show Decorations by Room ID");
            System.out.println("0. Back to Main Menu");

            option = scanner.readInt("Choose an option: ");

            try {
                switch (option) {
                    case 1 -> createDecoration();
                    case 2 -> getDecorationById();
                    case 3 -> listAllDecorations();
                    case 4 -> updateDecoration();
                    case 5 -> deleteDecoration();
                    case 6 -> getByRoomId();
                    case 0 -> System.out.println("Returning to main menu.");
                    default -> System.out.println("Invalid option.");
                }
            } catch (DecorationNotFoundException | SQLException e) {
                System.err.println("❌ Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Unexpected error: " + e.getMessage());
            }
        } while (option != 0);
    }

    // Crea un nuevo objeto de decoración
    private void createDecoration() throws SQLException {
        String name = scanner.readString("Decoration name: ");
        BigDecimal price = scanner.readBigDecimal("Price: ");
        Material material = scanner.readEnum(Material.class, "Choose a material:");
        int roomId = scanner.readInt("Room ID: ");

        DecorationObject decoration = new DecorationObject(name, price, material, roomId);
        decorationDao.createDecoration(decoration);
        System.out.println("✅ Decoration object created successfully.");
    }

    // Busca un objeto por su ID
    private void getDecorationById() throws SQLException {
        int id = scanner.readInt("Enter the ID of the decoration: ");
        DecorationObject d = decorationDao.getDecorationById(id);
        if (d != null) {
            System.out.println("🎯 Found: " + d);
        } else {
            System.out.println("⚠️ No decoration found with ID " + id);
        }
    }

    // Lista todos los objetos de decoración
    private void listAllDecorations() throws SQLException {
        List<DecorationObject> list = decorationDao.getAllDecorations();
        if (list.isEmpty()) {
            System.out.println("📭 No decorations available.");
        } else {
            list.forEach(System.out::println);
        }
    }

    // Actualiza un objeto de decoración existente
    private void updateDecoration() throws SQLException, DecorationNotFoundException {
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
    private void deleteDecoration() throws SQLException, DecorationNotFoundException {
        int id = scanner.readInt("ID of the decoration to delete: ");
        decorationDao.deleteDecoration(id);
        System.out.println("🗑️ Decoration deleted successfully.");
    }

    // Muestra todas las decoraciones asociadas a un roomId concreto
    private void getByRoomId() throws SQLException {
        int roomId = scanner.readInt("Enter the Room ID: ");
        List<DecorationObject> list = decorationDao.getDecorationsByRoomId(roomId);
        if (list.isEmpty()) {
            System.out.println("❌ No decorations found in that room.");
        } else {
            list.forEach(System.out::println);
        }
    }
}
