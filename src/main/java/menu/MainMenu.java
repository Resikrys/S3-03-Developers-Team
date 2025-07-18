package menu;

import util.DatabaseConnection;
import util.EnvLoader;
import manager.RoomManager;
import util.ScannerManager;


public class MainMenu {
    private final ScannerManager scannerManager;
    private final RoomManager roomManager;

    public MainMenu() {
        EnvLoader.getInstance();

        this.scannerManager = new ScannerManager();
        this.roomManager = new RoomManager(scannerManager);
    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations -> Rooms");
                System.out.println("0. Exit");
                input = scannerManager.readInt("Select option: ");

                switch (input) {
                    case 1:
                        roomManager.handleRoomCrud();
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