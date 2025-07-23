package menu;

import manager.PlayerManager;
import util.InputHelper;

import java.sql.SQLException;

public class PlayerMenu {

    private final PlayerManager playerManager;
    private final InputHelper inputHelper;

    public PlayerMenu(PlayerManager playerManager,InputHelper inputHelper) {
        this.playerManager = playerManager;
        this.inputHelper = inputHelper;
    }

    public void showMenu() {
        int option;
        do {
            System.out.println("\n--- Player Menu ---");
            System.out.println("1. Create player");
            System.out.println("2. List all players");
            System.out.println("3. Search player by ID");
            System.out.println("4. Update player");
            System.out.println("5. Delete player");
            System.out.println("0. Back to Main Menu");

            option = inputHelper.readInt("Select an option: ");

            try {
                switch (option) {
                    case 1:
                        playerManager.createPlayer();
                        break;
                    case 2:
                        playerManager.listAllPlayers();
                        break;
                    case 3:
                        playerManager.getPlayerById();
                        break;
                    case 4:
                        playerManager.updatePlayer();
                        break;
                    case 5:
                        playerManager.deletePlayer();
                        break;
                    case 0:
                        System.out.println("Returning to main menu...");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
                e.printStackTrace();
            }

        } while (option != 0);
    }
}
