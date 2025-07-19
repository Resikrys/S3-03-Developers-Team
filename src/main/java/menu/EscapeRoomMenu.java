package menu;

import manager.EscapeRoomManager;

import java.util.Scanner;

public class EscapeRoomMenu {
    private final EscapeRoomManager manager = new EscapeRoomManager();
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        int option;
        do {
            System.out.println("\n--- Escape Room Menu ---");
            System.out.println("1. Create EscapeRoom");
            System.out.println("2. List EscapeRooms");
            System.out.println("3. Update EscapeRoom");
            System.out.println("4. Delete EscapeRoom");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> manager.createEscapeRoom();
                case 2 -> manager.listEscapeRooms();
                case 3 -> manager.updateEscapeRoom();
                case 4 -> manager.deleteEscapeRoom();
                case 0 -> System.out.println("👋 Bye!");
                default -> System.out.println("❌ Invalid option.");
            }
        } while (option != 0);
    }
}
