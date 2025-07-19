package manager;

import dao.ClueDAOImplementation;
import dao.ClueDao;
import enums.Material;
import exception.InvalidInputException;
import model.ClueObject;
import util.SQLExecutor;
import util.ScannerManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ClueManager {
    private final ClueDao clueDAO;
    private final ScannerManager scanner;

    public ClueManager(ScannerManager scannerManager) {
        this.clueDAO = new ClueDAOImplementation(new SQLExecutor()); // DAO implementado
        this.scanner = scannerManager;
    }
    public void handleClueCrud() {
        int choice;
        do {
            System.out.println("\n--- CRUD for Clues ---");
            System.out.println("1. Create clue");
            System.out.println("2. List all clues");
            System.out.println("3. Update clue");
            System.out.println("4. Delete clue");
            System.out.println("5. Search clue by ID");
            System.out.println("6. List clues by Room ID");
            System.out.println("0. Back to main menu");
            choice = scanner.readInt("Select an option: ");

            try {
                switch (choice) {
                    case 1 -> createClue();
                    case 2 -> listAllClues();
                    case 3 -> updateClue();
                    case 4 -> deleteClue();
                    case 5 -> searchClueById();
                    case 6 -> listCluesByRoomId();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option.");
                }
            } catch (InvalidInputException e) {
                System.err.println("❌ Input error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Unexpected error: " + e.getMessage());
            }

        } while (choice != 0);
    }
    private void createClue() throws SQLException {
        String name = scanner.readString("Clue name: ");
        BigDecimal price = scanner.readBigDecimal("Clue price: ");
        Material material = scanner.readEnum(Material.class, "Material (WOOD, METAL, PLASTIC...): ");
        String puzzle = scanner.readString("Puzzle description: ");
        boolean solved = scanner.readBoolean("Is the clue solved? (true/false): ");
        int roomId = scanner.readInt("Room ID: ");

        ClueObject clue = new ClueObject(name, price, material, puzzle, solved, roomId);
        clueDAO.createClue(clue);
        System.out.println("✅ Clue created successfully!");
    }

    private void listAllClues() throws SQLException {
        List<ClueObject> clues = clueDAO.getAllClues();
        if (clues.isEmpty()) {
            System.out.println("No clues found.");
        } else {
            clues.forEach(System.out::println);
        }
    }

    private void updateClue() throws SQLException {
        int id = scanner.readInt("ID of clue to update: ");
        String newName = scanner.readString("New name: ");
        BigDecimal newPrice = scanner.readBigDecimal("New price: ");
        Material newMaterial = scanner.readEnum(Material.class, "New material: ");
        String newPuzzle = scanner.readString("New puzzle description: ");
        boolean newSolved = scanner.readBoolean("Is it solved? ");
        int newRoomId = scanner.readInt("New room ID: ");

        ClueObject updatedClue = new ClueObject(id, newName, newPrice, newMaterial, newPuzzle, newSolved, newRoomId);
        clueDAO.updateClue(updatedClue);
        System.out.println("✅ Clue updated.");
    }

    private void deleteClue() throws SQLException {
        int id = scanner.readInt("ID of clue to delete: ");
        clueDAO.deleteClue(id);
        System.out.println("✅ Clue deleted.");
    }
    // Buscar una pista por su ID
    private void searchClueById() throws SQLException {
        int id = scanner.readInt("Enter the ID of the clue: ");
        ClueObject clue = clueDAO.getClueById(id);
        if (clue != null) {
            System.out.println("✅ Clue found: " + clue);
        } else {
            System.out.println("⚠️ No clue found with ID " + id);
        }
    }

    // Listar todas las pistas de una sala concreta
    private void listCluesByRoomId() throws SQLException {
        int roomId = scanner.readInt("Enter the Room ID: ");
        List<ClueObject> clues = clueDAO.getCluesByRoomId(roomId);
        if (clues.isEmpty()) {
            System.out.println("⚠️ No clues found for room ID " + roomId);
        } else {
            System.out.println("✅ Clues in room " + roomId + ":");
            clues.forEach(System.out::println);
        }
    }

}

