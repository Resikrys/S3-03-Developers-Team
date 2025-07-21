package menu;

import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;
import manager.EscapeRoomManager;
import manager.RoomManager;
import util.InputHelper;
import manager.ClueManager;
import manager.DecorationManager;


public class MainMenu {
    private final InputHelper inputHelper;
    private final RoomManager roomManager;
    private final ClueManager clueManager;
    private final DecorationManager decorationManager;
    private final EscapeRoomManager escaperoomManager;
//    private final PlayerManager playermanager;
//    private final Ticketmanager ticketManager;
//    private final Inventory inventory;

    public MainMenu() {
        EnvLoader.getInstance();

        this.inputHelper = new InputHelper();
        this.escaperoomManager = new EscapeRoomManager(inputHelper);
        this.roomManager = new RoomManager(inputHelper);
        this.clueManager = new ClueManager(inputHelper);
        this.decorationManager = new DecorationManager(inputHelper);

    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations -> EscapeRoom");
                System.out.println("2. CRUD Operations -> Rooms");
                System.out.println("3. CRUD Operations -> Clues");
                System.out.println("4. CRUD Operations -> Decoration Objects");
                System.out.println("5. CRUD Operations -> Player");
                System.out.println("6. CRUD Operations -> Ticket");
                System.out.println("7. CRUD Operations -> Inventory");
                System.out.println("0. Exit");
                input = inputHelper.readInt("Select option: ");

                switch (input) {
                    case 1:
                        escaperoomManager.handleEscapeRoomCrud(); //Change to showMenu when submenu available
                        break;
                    case 2:
                        roomManager.showMenu();
                        break;
                    case 3:
                        clueManager.showMenu();
                        break;
                    case 4:
                        decorationManager.showMenu();
                        break;
                    case 5:
                        //playerManager.showMenu();
                        break;
                    case 6:
                        //ticketManager.showMenu();
                        break;
                    case 7:
                        //inventory.showMenu();
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