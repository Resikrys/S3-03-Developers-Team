package menu;

import dao.*;
import dbconnection.DatabaseConnection;
import dbconnection.EnvLoader;
import dbconnection.SQLExecutor;
import manager.*;
import manager.NotificationManager;
import util.InputHelper;

public class MainMenu {
    private final InputHelper inputHelper;
    private final EscapeRoomMenu escapeRoomMenu;
    private final RoomMenu roomMenu;
    private final ClueMenu clueMenu;
    private final DecorationMenu decorationMenu;
    private final InventoryService inventoryService;
    private final PlayerMenu playerMenu;

    public MainMenu() {
        EnvLoader.getInstance(); // Carga .env si lo usas

        this.inputHelper = new InputHelper();
        SQLExecutor executor = new SQLExecutor();

        // DAO y managers
        PlayerDao playerDao = new PlayerDaoImplementation(executor);
        PlayerManager playerManager = new PlayerManager(playerDao, inputHelper);
        NotificationManager notificationManager = new NotificationManager();  // Observer aquí

        // Menús
        this.escapeRoomMenu = new EscapeRoomMenu(inputHelper);
        this.roomMenu = new RoomMenu(inputHelper, playerDao, notificationManager);  // Notificación integrada
        this.clueMenu = new ClueMenu(inputHelper);
        this.decorationMenu = new DecorationMenu(inputHelper);
        this.playerMenu = new PlayerMenu(playerManager, inputHelper);

        this.inventoryService = new InventoryService(
                new RoomDaoImplementation(),
                new ClueDAOImplementation(),
                new DecorationDAOImplementation()
        );
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
                System.out.println("6. CRUD Operations -> Ticket (Not implemented)");
                System.out.println("7. Inventory Overview");
                System.out.println("0. Exit");

                input = inputHelper.readInt("Select option: ");

                switch (input) {
                    case 1 -> escapeRoomMenu.showMenu();
                    case 2 -> roomMenu.showMenu();
                    case 3 -> clueMenu.showMenu();
                    case 4 -> decorationMenu.showMenu();
                    case 5 -> playerMenu.showMenu();
                    case 6 -> System.out.println("⚠️ Ticket module not yet implemented.");
                    case 7 -> inventoryService.showMenu(inputHelper);
                    case 0 -> System.out.println("👋 Exiting the application. See you soon!");
                    default -> System.out.println("❌ Invalid option. Please try again.");
                }
            } while (input != 0);

        } catch (RuntimeException e) {
            System.err.println("💥 Fatal error launching application: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error in Main Menu: " + e.getMessage());
            e.printStackTrace();
        } finally {
            inputHelper.closeScanner();
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}