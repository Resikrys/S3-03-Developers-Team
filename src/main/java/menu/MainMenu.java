package menu;

import dao.*;
import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;
import dbconnection.MongoDBConnection;
import dbconnection.SQLExecutor;
import manager.InventoryService;
import manager.PlayerManager;
import menu.TicketMenu;
import util.InputHelper;


public class MainMenu {
    private final InputHelper inputHelper;
    private final EscapeRoomMenu escapeRoomMenu;
    private final RoomMenu roomMenu; // Now it's RoomMenu, not RoomManager
    private final ClueMenu clueMenu;
    private final DecorationMenu decorationMenu;
    private final PlayerMenu playerMenu;
    private final InventoryService inventoryService;
    private final RewardMenu rewardMenu;
    private final TicketMenu ticketMenu;

//    private final Ticketmanager ticketManager;

    public MainMenu() {
        EnvLoader.getInstance();
        // 2. Initialize database connections
        // This connects to MySQL
        DatabaseConnection.getInstance();
        // This connects to MongoDB
        MongoDBConnection.getDatabaseInstance();

        this.inputHelper = new InputHelper();
        this.escapeRoomMenu = new EscapeRoomMenu(inputHelper);
        this.roomMenu  = new RoomMenu(inputHelper);
        this.clueMenu = new ClueMenu(inputHelper);
        this.decorationMenu = new DecorationMenu(inputHelper);
        PlayerDao playerDao = new PlayerDaoImplementation(new SQLExecutor()); //!!
        PlayerManager playerManager = new PlayerManager(playerDao, inputHelper); //!!
        this.playerMenu = new PlayerMenu(playerManager, inputHelper); //!!
        this.ticketMenu = new TicketMenu(inputHelper);
        this.inventoryService = new InventoryService(
                new RoomDaoImplementation(),
                new ClueDAOImplementation(),
                new DecorationDAOImplementation()
        );
        this.rewardMenu = new RewardMenu(inputHelper);
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
                System.out.println("8. CRUD Operations -> Rewards");
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
                        playerMenu.showMenu();
                        break;
                    case 6:
                        ticketMenu.showMenu();
                        break;
                    case 7:
                        inventoryService.showMenu(inputHelper);
                        break;
                    case 8:
                        rewardMenu.showMenu();
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
            MongoDBConnection.closeConnection();
        }
    }
}