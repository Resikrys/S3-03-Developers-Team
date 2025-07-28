package menu;

import exception.InvalidInputException;
import exception.RoomNotFoundException;
import manager.RoomManager;
import util.InputHelper;

import java.sql.SQLException;

public class RoomMenu {
    private final InputHelper inputHelper;
    private final RoomManager roomManager;

    public RoomMenu(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
        this.roomManager = new RoomManager(inputHelper);
    }

    public void showMenu() {
        int roomChoice;
        do {
            System.out.println("\n--- CRUD for Rooms ---");
            System.out.println("1. Create Room");
            System.out.println("2. Search Room by ID");
            System.out.println("3. List all Rooms available");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to Main Menu");
            roomChoice = inputHelper.readInt("Select a valid option for Rooms Menu: ");

            try {
                switch (roomChoice) {
                    case 1:
                        roomManager.createRoom();
                        break;
                    case 2:
                        roomManager.searchRoomById();
                        break;
                    case 3:
                        roomManager.listAllRooms();
                        break;
                    case 4:
                        roomManager.updateRoom();
                        break;
                    case 5:
                        roomManager.deleteRoom();
                        break;
                    case 0:
                        System.out.println("Back to Main menu.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (InvalidInputException e) {
                System.err.println("❌ Input error: " + e.getMessage() + ". Please try again.");
            } catch (RoomNotFoundException e) {
                System.err.println("❌ Error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Database error in Room operation: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("❌ Unexpected error in Room operation: " + e.getMessage());
                e.printStackTrace();
            }
        } while (roomChoice != 0);
    }
}
