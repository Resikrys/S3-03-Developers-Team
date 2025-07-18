package menu;

import dao.RoomDao;
import dao.RoomDaoImplementation;
import model.Room;
import util.DatabaseConnection;
import util.EnvLoader;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private RoomDao roomDao;
    private Scanner scanner;

    public MainMenu() {
        EnvLoader.getInstance();

        this.roomDao = new RoomDaoImplementation();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations -> Rooms");
                System.out.println("0. Exit");
                System.out.print("Select option: ");
                input = scanner.nextInt();
                scanner.nextLine();

                switch (input) {
                    case 1:
                        handleRoomCrud();
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
            if (scanner != null) {
                scanner.close();
            }
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    private void handleRoomCrud() {
        int roomChoice;
        do {
            System.out.println("\n--- CRUD for Rooms ---");
            System.out.println("1. Create Room"); // Cambiado a Crear
            System.out.println("2. Search Room by ID");
            System.out.println("3. List all Rooms available");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select a valid option of Rooms Menu: ");
            roomChoice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (roomChoice) {
                    case 1:
                        System.out.print("Theme: "); String theme = scanner.nextLine();
                        System.out.print("Difficulty level (1-10): "); int difficulty = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("ID of the Escape Room it belongs to (leave blank if not applicable): ");
                        String escapeRoomIdInput = scanner.nextLine();
                        Integer escapeRoomId = null;
                        if (!escapeRoomIdInput.trim().isEmpty()) {
                            try {
                                escapeRoomId = Integer.parseInt(escapeRoomIdInput);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid Escape Room ID. It will be set to NULL.");
                            }
                        }
                        roomDao.createRoom(new Room(theme, difficulty, escapeRoomId));
                        break;
                    case 2:
                        System.out.print("ID of the Room to search: "); int idGet = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> optionalRoom = roomDao.getRoomById(idGet);
                        if (optionalRoom.isPresent()) {
                            System.out.println("Room finded: " + optionalRoom.get());
                        } else {
                            System.out.println("Room with ID " + idGet + " not available.");
                        }
                        break;
                    case 3:
                        List<Room> rooms = roomDao.getAllRooms();
                        if (rooms.isEmpty()) System.out.println("No rooms available.");
                        else rooms.forEach(System.out::println);
                        break;
                    case 4:
                        System.out.print("ID of the Room to be updated: "); int idUpdate = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> roomToUpdateOptional = roomDao.getRoomById(idUpdate);
                        if (roomToUpdateOptional.isPresent()) {
                            Room roomToUpdate = roomToUpdateOptional.get();
                            System.out.print("New theme (actual: " + roomToUpdate.getTheme() + "): "); String newTheme = scanner.nextLine();
                            System.out.print("New difficulty level (actual: " + roomToUpdate.getDifficultyLevel() + "): "); int newDifficulty = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("New ID of the Escape Room (actual: " + (roomToUpdate.getEscapeRoomId() != null ? roomToUpdate.getEscapeRoomId() : "NULL") + ", leave blank if not applicable): ");
                            String newEscapeRoomIdInput = scanner.nextLine();
                            Integer newEscapeRoomId = null;
                            if (!newEscapeRoomIdInput.trim().isEmpty()) {
                                try {
                                    newEscapeRoomId = Integer.parseInt(newEscapeRoomIdInput);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid Escape Room ID. Current value will be kept.");
                                    newEscapeRoomId = roomToUpdate.getEscapeRoomId();
                                }
                            }
                            roomToUpdate.setTheme(newTheme);
                            roomToUpdate.setDifficultyLevel(newDifficulty);
                            roomToUpdate.setEscapeRoomId(newEscapeRoomId);
                            roomDao.updateRoom(roomToUpdate);
                        } else {
                            System.out.println("Room with ID " + idUpdate + " not found to update.");
                        }
                        break;
                    case 5:
                        System.out.print("ID of the Room to be deleted: "); int idDelete = scanner.nextInt();
                        scanner.nextLine();
                        roomDao.deleteRoom(idDelete);
                        break;
                    case 0:
                        System.out.println("Back to Main menu.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.err.println("Database error in Room operation: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) { // Captura cualquier otra excepción general
                System.err.println("Unexpected error in Room operation: " + e.getMessage());
                e.printStackTrace();
            }
        } while (roomChoice != 0);
    }
}