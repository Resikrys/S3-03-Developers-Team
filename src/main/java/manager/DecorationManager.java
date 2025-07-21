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
import java.util.Optional;

public class DecorationManager {
    private final DecorationObjectDAO decorationDao;
    private final InputHelper inputHelper; // Renamed for consistency

    public DecorationManager(InputHelper inputHelper) {
        this.decorationDao = new DecorationDAOImplementation();
        this.inputHelper = inputHelper;
    }

    // The handleDecorationCrud() method is REMOVED from here and moved to DecorationMenu.

    public void createDecoration() throws SQLException { // Made public
        String name = inputHelper.readString("Decoration name: ");
        BigDecimal price = inputHelper.readBigDecimal("Price: ");
        // Ensure readEnum handles invalid input and throws InvalidInputException
        Material material = inputHelper.readEnum(Material.class, "Choose a material (WOOD, METAL, PLASTIC, etc.): ");
        int roomId = inputHelper.readInt("Room ID: ");

        DecorationObject decoration = new DecorationObject(name, price, material, roomId);
        decorationDao.createDecoration(decoration);
        // Confirmation message is now handled in DAO
    }

    public void getDecorationById() throws SQLException { // Made public
        int id = inputHelper.readInt("Enter the ID of the decoration to search: ");
        Optional<DecorationObject> optionalDecoration = decorationDao.getDecorationById(id); // Use Optional

        if (optionalDecoration.isPresent()) {
            System.out.println("🎯 Found: " + optionalDecoration.get());
        } else {
            System.out.println("⚠️ No decoration found with ID " + id);
        }
    }

    public void listAllDecorations() throws SQLException { // Made public
        List<DecorationObject> list = decorationDao.getAllDecorations();
        if (list.isEmpty()) {
            System.out.println("📭 No decorations available.");
        } else {
            System.out.println("--- All Decorations ---");
            list.forEach(System.out::println);
        }
    }

    public void updateDecoration() throws SQLException, DecorationNotFoundException { // Made public
        int id = inputHelper.readInt("ID of the decoration to update: ");
        Optional<DecorationObject> optionalDecoration = decorationDao.getDecorationById(id); // Use Optional

        if (optionalDecoration.isPresent()) {
            DecorationObject decorationToUpdate = optionalDecoration.get();
            System.out.println("Current details for Decoration ID " + id + ": " + decorationToUpdate);

            String newName = inputHelper.readString("New name (current: " + decorationToUpdate.getName() + "): ");
            BigDecimal newPrice = inputHelper.readBigDecimal("New price (current: " + decorationToUpdate.getPrice() + "): ");
            // Handle enum input more robustly: use readEnum
            Material newMaterial = inputHelper.readEnum(Material.class, "New material (current: " + decorationToUpdate.getMaterial().name() + "): ");
            int newRoomId = inputHelper.readInt("New room ID (current: " + decorationToUpdate.getRoomId() + "): ");

            decorationToUpdate.setName(newName);
            decorationToUpdate.setPrice(newPrice);
            decorationToUpdate.setMaterial(newMaterial);
            decorationToUpdate.setRoomId(newRoomId);

            decorationDao.updateDecoration(decorationToUpdate);
            // Confirmation message handled in DAO
        } else {
            // No need to throw here, just inform the user
            System.out.println("⚠️ Decoration with ID " + id + " not found for update.");
        }
    }

    public void deleteDecoration() throws SQLException, DecorationNotFoundException { // Made public
        int id = inputHelper.readInt("ID of the decoration to delete: ");
        decorationDao.deleteDecoration(id);
        // Confirmation message handled in DAO
    }

    public void getByRoomId() throws SQLException { // Made public
        int roomId = inputHelper.readInt("Enter the Room ID to list decorations for: ");
        List<DecorationObject> list = decorationDao.getDecorationsByRoomId(roomId);
        if (list.isEmpty()) {
            System.out.println("❌ No decorations found in room ID " + roomId + ".");
        } else {
            System.out.println("--- Decorations in Room " + roomId + " ---");
            list.forEach(System.out::println);
        }
    }
}
