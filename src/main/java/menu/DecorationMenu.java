package menu;

import exception.DecorationNotFoundException;
import exception.InvalidInputException;
import manager.DecorationManager;
import util.InputHelper;

import java.sql.SQLException;

public class DecorationMenu {
    private final DecorationManager decorationManager;
    private final InputHelper inputHelper; // Renamed for consistency

    public DecorationMenu(InputHelper inputHelper) {
        this.inputHelper = inputHelper; // Initialize inputHelper
        this.decorationManager = new DecorationManager(inputHelper);
    }

    public void showMenu() {
        int option;
        do {
            printMenu();
            option = inputHelper.readInt("Choose an option: "); // Use inputHelper

            try {
                handleOption(option);
            } catch (InvalidInputException e) { // Catch InputHelper exceptions
                System.err.println("❌ Input error: " + e.getMessage() + ". Please try again.");
            } catch (DecorationNotFoundException e) {
                System.err.println("❌ Operation error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("❌ Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }

        } while (option != 0);
    }

    private void printMenu() {
        System.out.println("\n--- Decoration Objects CRUD ---");
        System.out.println("1. Create Decoration");
        System.out.println("2. Search Decoration by ID");
        System.out.println("3. List All Decorations");
        System.out.println("4. Update Decoration");
        System.out.println("5. Delete Decoration");
        System.out.println("6. Show Decorations by Room ID");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: "); // Added prompt to the menu itself for clarity
    }

    // Now declares the specific exceptions that manager methods can throw
    private void handleOption(int option) throws SQLException, DecorationNotFoundException {
        switch (option) {
            case 1 -> decorationManager.createDecoration();
            case 2 -> decorationManager.getDecorationById();
            case 3 -> decorationManager.listAllDecorations();
            case 4 -> decorationManager.updateDecoration();
            case 5 -> decorationManager.deleteDecoration();
            case 6 -> decorationManager.getByRoomId();
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Invalid option.");
        }
    }
}