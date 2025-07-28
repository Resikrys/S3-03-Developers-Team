package manager;

import dao.DecorationDAOImplementation;
import dao.DecorationObjectDAO;
import enums.Material;
import exception.DecorationNotFoundException;
import model.DecorationObject;
import util.InputHelper;
import observer.NotificationService;
import observer.NotificationEvent;
import observer.EventType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DecorationManager {
    private final DecorationObjectDAO decorationDao;
    private final InputHelper inputHelper;

    public DecorationManager(InputHelper inputHelper) {
        this.decorationDao = new DecorationDAOImplementation();
        this.inputHelper = inputHelper;
    }

    public void createDecoration() throws SQLException {
        String name = inputHelper.readString("Decoration name: ");
        BigDecimal price = inputHelper.readBigDecimal("Price: ");
        Material material = inputHelper.readEnum(Material.class, "Choose a material (WOOD, METAL, PLASTIC, etc.): ");
        int roomId = inputHelper.readInt("Room ID: ");

        DecorationObject decoration = new DecorationObject(name, price, material, roomId);
        decorationDao.createDecoration(decoration);
        NotificationService.getInstance().notifyObservers(
                new NotificationEvent(EventType.DECORATION_CREATED, "DecorationObject", decoration.getId(), decoration.getName())
        );
    }

    public void getDecorationById() throws SQLException {
        int id = inputHelper.readInt("Enter the ID of the decoration to search: ");
        Optional<DecorationObject> optionalDecoration = decorationDao.getDecorationById(id);

        if (optionalDecoration.isPresent()) {
            System.out.println("🎯 Found: " + optionalDecoration.get());
        } else {
            System.out.println("⚠️ No decoration found with ID " + id);
        }
    }

    public void listAllDecorations() throws SQLException {
        List<DecorationObject> list = decorationDao.getAllDecorations();
        if (list.isEmpty()) {
            System.out.println("📭 No decorations available.");
        } else {
            System.out.println("--- All Decorations ---");
            list.forEach(System.out::println);
        }
    }

    public void updateDecoration() throws SQLException, DecorationNotFoundException {
        int id = inputHelper.readInt("ID of the decoration to update: ");
        Optional<DecorationObject> optionalDecoration = decorationDao.getDecorationById(id);

        if (optionalDecoration.isPresent()) {
            DecorationObject decorationToUpdate = optionalDecoration.get();
            String oldName = decorationToUpdate.getName();
            System.out.println("Current details for Decoration ID " + id + ": " + decorationToUpdate);

            String newName = inputHelper.readString("New name (current: " + decorationToUpdate.getName() + "): ");
            BigDecimal newPrice = inputHelper.readBigDecimal("New price (current: " + decorationToUpdate.getPrice() + "): ");
            Material newMaterial = inputHelper.readEnum(Material.class, "New material (current: " + decorationToUpdate.getMaterial().name() + "): ");
            int newRoomId = inputHelper.readInt("New room ID (current: " + decorationToUpdate.getRoomId() + "): ");

            decorationToUpdate.setName(newName);
            decorationToUpdate.setPrice(newPrice);
            decorationToUpdate.setMaterial(newMaterial);
            decorationToUpdate.setRoomId(newRoomId);

            decorationDao.updateDecoration(decorationToUpdate);
            String updatedDescription = oldName + " -> " + newName;
            NotificationService.getInstance().notifyObservers(
                    new NotificationEvent(EventType.DECORATION_UPDATED, "DecorationObject", decorationToUpdate.getId(), updatedDescription)
            );
        } else {
            System.out.println("⚠️ Decoration with ID " + id + " not found for update.");
        }
    }

    public void deleteDecoration() throws SQLException, DecorationNotFoundException {
        int id = inputHelper.readInt("ID of the decoration to delete: ");
        Optional<DecorationObject> decorationToDeleteOptional = decorationDao.getDecorationById(id);
        decorationDao.deleteDecoration(id);
        decorationToDeleteOptional.ifPresent(decoration ->
                NotificationService.getInstance().notifyObservers(
                        new NotificationEvent(EventType.DECORATION_DELETED, "DecorationObject", decoration.getId(), decoration.getName())
                )
        );
    }

    public void getByRoomId() throws SQLException {
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
