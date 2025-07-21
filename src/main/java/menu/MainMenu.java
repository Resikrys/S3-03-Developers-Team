package menu;

import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;
import manager.RoomManager;
import util.InputHelper;
import manager.ClueManager;
import manager.DecorationManager;


public class MainMenu {
    private final InputHelper inputHelper;
    private final RoomManager roomManager;
    private final ClueManager clueManager;
    private final DecorationManager decorationManager;

    public MainMenu() {
        EnvLoader.getInstance();

        this.inputHelper = new InputHelper();
        this.roomManager = new RoomManager(inputHelper);
        this.clueManager = new ClueManager(inputHelper);
        this.decorationManager = new DecorationManager(inputHelper);

    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations -> Rooms");
                System.out.println("2. CRUD Operations -> Clues");
                System.out.println("3. CRUD Operations -> Decoration Objects");
                System.out.println("0. Exit");
                input = inputHelper.readInt("Select option: ");

                switch (input) {
                    case 1:
                        roomManager.handleRoomCrud();
                        break;
                    case 2:
                        clueManager.handleClueCrud();
                        break;
                    case 3:
                        decorationManager.handleDecorationCrud();  // ✅ Añadida opción para objetos de decoración
                        break;

                    case 0:
                        System.out.println("Exiting the application. See you soon!");
                        break;
                    default:
                        System.out.println("Invalid option. Select again: ");
                }
            } while (input != 0);

        } catch (RuntimeException e) {
            System.err.println("Fatal error launching application: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in the main menu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            inputHelper.closeScanner();
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}