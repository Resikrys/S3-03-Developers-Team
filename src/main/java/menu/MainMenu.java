package menu;

import dao.*;
import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;
import manager.EscapeRoomManager;
import manager.InventoryService;
import util.InputHelper;
import manager.InventoryService;


public class MainMenu {
    private final InputHelper inputHelper;
    private final EscapeRoomMenu escapeRoomMenu;
    private final RoomMenu roomMenu; // Now it's RoomMenu, not RoomManager
    private final ClueMenu clueMenu;
    private final DecorationMenu decorationMenu;
    private final InventoryService inventoryService;
//    private final ClueManager clueManager; // Assuming these will also get their own menus later
//    private final DecorationManager decorationManager;
//    private final EscapeRoomManager escaperoomManager;
//    private final EscapeRoomManager escaperoomManager;
//    private final PlayerManager playermanager;
//    private final Ticketmanager ticketManager;
//    private final Inventory inventory;

    public MainMenu() {
        EnvLoader.getInstance();

        this.inputHelper = new InputHelper();
        this.escapeRoomMenu = new EscapeRoomMenu(inputHelper);
        this.roomMenu  = new RoomMenu(inputHelper); //This works OK!!
        this.clueMenu = new ClueMenu(inputHelper);
        this.decorationMenu = new DecorationMenu(inputHelper);
        this.inventoryService = new InventoryService(
                new RoomDaoImplementation(),
                new ClueDAOImplementation(),
                new DecorationDAOImplementation()
        );

//        this.playerManager = new PlayerManager(inputHelper);
//        this.ticketManager = new TicketManager(inputHelper);


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
                        escapeRoomMenu.showMenu();
                        break;
                    case 2:
                        roomMenu .showMenu();
                        break;
                    case 3:
                        clueMenu.showMenu();
                        break;
                    case 4:
                        decorationMenu.showMenu();
                        break;
                    case 5:
                        //playerManager.showMenu();
                        break;
                    case 6:
                        //ticketManager.showMenu();
                        break;
                    case 7:
                        inventoryService.showMenu(inputHelper);
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