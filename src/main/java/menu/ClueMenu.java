package menu;

import exception.InvalidInputException;
import manager.ClueManager;
import util.InputHelper;

import java.sql.SQLException;

public class ClueMenu {
    private final ClueManager clueManager;
    private final InputHelper scanner;

    public ClueMenu(InputHelper inputHelper) {
        this.clueManager = new ClueManager(inputHelper);
        this.scanner = inputHelper;
    }

    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = scanner.readInt("Select an option: ");

            try {
                handleChoice(choice);
            } catch (InvalidInputException e) {
                System.err.println("❌ Input error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Unexpected error: " + e.getMessage());
            }

        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n--- CRUD for Clues ---");
        System.out.println("1. Create clue");
        System.out.println("2. List all clues");
        System.out.println("3. Update clue");
        System.out.println("4. Delete clue");
        System.out.println("5. Search clue by ID");
        System.out.println("6. List clues by Room ID");
        System.out.println("0. Back to main menu");
    }

    private void handleChoice(int choice) throws Exception {
        switch (choice) {
            case 1 -> clueManager.createClue();
            case 2 -> clueManager.listAllClues();
            case 3 -> clueManager.updateClue();
            case 4 -> clueManager.deleteClue();
            case 5 -> clueManager.searchClueById();
            case 6 -> clueManager.listCluesByRoomId();
            case 0 -> System.out.println("Returning to main menu...");
            default -> System.out.println("Invalid option.");
        }
    }
}
