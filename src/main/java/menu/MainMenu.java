package menu;

import util.DatabaseConnection;
import util.EnvLoader;
import manager.RoomManager;
import util.ScannerManager;
import manager.ClueManager;
import manager.DecorationManager;


public class MainMenu {
    private final ScannerManager scannerManager;
    private final RoomManager roomManager;
    private final ClueMenu clueMenu;
    private final DecorationMenu decorationMenu;

    public MainMenu() {
        EnvLoader.getInstance();

        this.scannerManager = new ScannerManager();
        this.roomManager = new RoomManager(scannerManager);
        this.clueMenu = new ClueMenu(scannerManager);
        this.decorationMenu = new DecorationMenu(scannerManager);

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
                input = scannerManager.readInt("Select option: ");

                switch (input) {
                    case 1:
                        roomManager.handleRoomCrud();
                        break;
                    case 2:
                        clueMenu.showMenu();
                        break;
                    case 3:
                        decorationMenu.showMenu();  // ✅ Añadida opción para objetos de decoración
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
            scannerManager.closeScanner();
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}