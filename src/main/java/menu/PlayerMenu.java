package menu;

import manager.PlayerManager;
import util.InputHelper;

public class PlayerMenu {
    private final PlayerManager manager = new PlayerManager();

    public void show() {
        int option;
        do {
            System.out.println("\n--- Player Menu ---");
            System.out.println("1. Create Player");
            System.out.println("2. List Players");
            System.out.println("3. Update Player");
            System.out.println("4. Delete Player");
            System.out.println("0. Back to Main Menu");
            option = InputHelper.readInt("Choose an option: ");

            switch (option) {
                case 1 -> manager.createPlayer();
                case 2 -> manager.listPlayers();
                case 3 -> manager.updatePlayer();
                case 4 -> manager.deletePlayer();
                case 0 -> System.out.println("⬅️ Returning to main menu...");
                default -> System.out.println("❌ Invalid option.");
            }
        } while (option != 0);
    }
}
