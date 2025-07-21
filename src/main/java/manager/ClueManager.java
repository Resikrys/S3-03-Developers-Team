package manager;

import dao.ClueDAOImplementation;
import dao.ClueDao;
import enums.Material;
import exception.NotFoundException;
import model.ClueObject;
import dbconnection.SQLExecutor;
import util.InputHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClueManager {
    private final ClueDao clueDAO;
    private final InputHelper inputHelper; // Renamed 'scanner' to 'inputHelper' for consistency

    public ClueManager(InputHelper inputHelper) {
        // Now ClueDAOImplementation can be instantiated directly
        this.clueDAO = new ClueDAOImplementation();
        this.inputHelper = inputHelper;
    }

    // Moved showMenu to ClueMenu as per your new structure

    public void createClue() throws SQLException {
        String name = inputHelper.readString("Enter clue name: ");
        BigDecimal price = inputHelper.readBigDecimal("Enter clue price: ");
        Material material = inputHelper.readEnum(Material.class, "Enter material (WOOD, METAL, PLASTIC, etc.): ");
        String puzzle = inputHelper.readString("Enter puzzle description: ");
        boolean solved = inputHelper.readBoolean("Is the clue solved? (true/false): ");
        int roomId = inputHelper.readInt("Enter Room ID: ");

        ClueObject clue = new ClueObject(name, price, material, puzzle, solved, roomId);
        clueDAO.createClue(clue);
        // Confirmation message is now handled in DAO
    }

    public void listAllClues() throws SQLException {
        List<ClueObject> clues = clueDAO.getAllClues();
        if (clues.isEmpty()) {
            System.out.println("⚠️ No clues found.");
        } else {
            System.out.println("--- All Clues ---");
            clues.forEach(System.out::println);
        }
    }

    public void updateClue() throws SQLException, NotFoundException { // Declares ClueNotFoundException
        int id = inputHelper.readInt("Enter ID of clue to update: ");
        Optional<ClueObject> existingClue = clueDAO.getClueById(id);

        if (existingClue.isPresent()) {
            ClueObject clueToUpdate = existingClue.get();
            System.out.println("Current details for Clue ID " + id + ": " + clueToUpdate);

            String newName = inputHelper.readString("New name (current: " + clueToUpdate.getName() + "): ");
            BigDecimal newPrice = inputHelper.readBigDecimal("New price (current: " + clueToUpdate.getPrice() + "): ");
            Material newMaterial = inputHelper.readEnum(Material.class, "New material (current: " + clueToUpdate.getMaterial().name() + "): ");
            String newPuzzle = inputHelper.readString("New puzzle description (current: " + clueToUpdate.getPuzzleDescription() + "): ");
            boolean newSolved = inputHelper.readBoolean("Is it solved? (current: " + clueToUpdate.isSolved() + ", true/false): ");
            int newRoomId = inputHelper.readInt("New room ID (current: " + clueToUpdate.getRoomId() + "): ");

            clueToUpdate.setName(newName);
            clueToUpdate.setPrice(newPrice);
            clueToUpdate.setMaterial(newMaterial);
            clueToUpdate.setPuzzleDescription(newPuzzle);
            clueToUpdate.setSolved(newSolved);
            clueToUpdate.setRoomId(newRoomId);

            clueDAO.updateClue(clueToUpdate); // This can throw ClueNotFoundException
            // Confirmation message handled in DAO
        } else {
            System.out.println("⚠️ Clue with ID " + id + " not found for update.");
        }
    }

    public void deleteClue() throws SQLException, NotFoundException { // Declares ClueNotFoundException
        int id = inputHelper.readInt("Enter ID of clue to delete: ");
        clueDAO.deleteClue(id); // This can throw ClueNotFoundException
        // Confirmation message handled in DAO
    }

    public void searchClueById() throws SQLException { // No ClueNotFoundException here, as Optional handles absence
        int id = inputHelper.readInt("Enter the ID of the clue to search: ");
        Optional<ClueObject> optionalClue = clueDAO.getClueById(id); // Now returns Optional

        if (optionalClue.isPresent()) {
            System.out.println("✅ Clue found: " + optionalClue.get());
        } else {
            System.out.println("⚠️ No clue found with ID " + id);
        }
    }

    public void listCluesByRoomId() throws SQLException {
        int roomId = inputHelper.readInt("Enter the Room ID to list clues for: ");
        List<ClueObject> clues = clueDAO.getCluesByRoomId(roomId);
        if (clues.isEmpty()) {
            System.out.println("⚠️ No clues found for room ID " + roomId);
        } else {
            System.out.println("--- Clues in room " + roomId + " ---");
            clues.forEach(System.out::println);
        }
    }
}