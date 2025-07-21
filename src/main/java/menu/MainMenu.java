package menu;

import dao.*;
import manager.InventoryService;
import util.DatabaseConnection;
import util.EnvLoader;
import manager.RoomManager;
import util.InputHelper;
import util.SQLExecutor;


public class MainMenu {
    private final InputHelper inputHelper;
    private final RoomManager roomManager;
    private final ClueMenu clueMenu;
    private final DecorationMenu decorationMenu;
    private final InventoryService inventoryService;

    public MainMenu() {
        EnvLoader.getInstance();

        this.inputHelper = new InputHelper();
        this.roomManager = new RoomManager(inputHelper);
        this.clueMenu = new ClueMenu(inputHelper);
        this.decorationMenu = new DecorationMenu(inputHelper);

        RoomDao roomDao = new RoomDaoImplementation();
        ClueDao clueDao = new ClueDAOImplementation(new SQLExecutor());
        DecorationObjectDAO decorationDao = new DecorationDAOImplementation();

        this.inventoryService = new InventoryService(roomDao, clueDao, decorationDao);

    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations -> Rooms");
                System.out.println("2. CRUD Operations -> Clues");
                System.out.println("3. CRUD Operations -> Decoration Objects");
                System.out.println("4. Inventory Menu");
                System.out.println("0. Exit");
                input = inputHelper.readInt("Select option: ");

                switch (input) {
                    case 1:
                        roomManager.handleRoomCrud();
                        break;
                    case 2:
                        clueMenu.showMenu();
                        break;
                    case 3:
                        decorationMenu.showMenu();
                        break;
                    case 4:
                        inventoryService.showInteractiveInventoryMenu(inputHelper);
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