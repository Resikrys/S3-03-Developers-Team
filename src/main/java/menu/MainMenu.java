package menu;

import util.InputHelper;

public class MainMenu {

    public void show() {
        int option;
        do {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. EscapeRoom Menu");
            System.out.println("2. Player Menu");
            System.out.println("0. Exit");
            option = InputHelper.readInt("Choose an option: ");

            switch (option) {
                case 1 -> new EscapeRoomMenu().show();
                case 2 -> new PlayerMenu().show();
                case 0 -> System.out.println("👋 Goodbye!");
                default -> System.out.println("❌ Invalid option.");
            }
        } while (option != 0);
    }

}
