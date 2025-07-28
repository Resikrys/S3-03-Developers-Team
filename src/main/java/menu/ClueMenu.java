package menu;

import exception.InvalidInputException;
import exception.NotFoundException;
import manager.ClueManager;
import util.InputHelper;

import java.sql.SQLException;

public class ClueMenu {
    private final ClueManager clueManager;
    private final InputHelper inputHelper;

    public ClueMenu(InputHelper inputHelper) {
        this.clueManager = new ClueManager(inputHelper);
        this.inputHelper = inputHelper;
    }

    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = inputHelper.readInt("Select an option: ");

            try {
                handleChoice(choice);
            } catch (InvalidInputException e) {
                System.err.println("❌ Input error: " + e.getMessage() + ". Please try again.");
            } catch (NotFoundException e) {
                System.err.println("❌ Operation error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("❌ Unexpected error: " + e.getMessage());
                e.printStackTrace();
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
        System.out.print("Select an option: ");
    }

    private void handleChoice(int choice) throws SQLException, NotFoundException {
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