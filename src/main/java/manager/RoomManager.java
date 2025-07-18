package manager;

import dao.RoomDao;
import dao.RoomDaoImplementation;
import exception.RoomNotFoundException;
import model.Room;
import util.ScannerManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoomManager {
    private final RoomDao roomDao;
    private final ScannerManager scanner;

    public RoomManager(ScannerManager scannerManager) {
        this.roomDao = new RoomDaoImplementation();
        this.scanner = scannerManager;
    }

    public void handleRoomCrud() {
        int roomChoice;
        do {
            System.out.println("\n--- CRUD for Rooms ---");
            System.out.println("1. Create Room");
            System.out.println("2. Search Room by ID");
            System.out.println("3. List all Rooms available");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select a valid option of Rooms Menu: ");
            roomChoice = scanner.readInt("Select a valid option of Rooms Menu: ");

            try {
                switch (roomChoice) {
                    case 1:
                        createRoom();
                        break;
                    case 2:
                        searchRoomById();
                        break;
                    case 3:
                        listAllRooms();
                        break;
                    case 4:
                        updateRoom();
                        break;
                    case 5:
                        deleteRoom();
                        break;
                    case 0:
                        System.out.println("Back to Main menu.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (RoomNotFoundException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Database error in Room operation: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Unexpected error in Room operation: " + e.getMessage());
                e.printStackTrace();
            }
        } while (roomChoice != 0);
    }

    private void createRoom() throws SQLException {
        String theme = scanner.readString("Theme: ");
        int difficulty = scanner.readInt("Difficulty level (1-10): ");
        Integer escapeRoomId = scanner.readOptionalInt("ID of the Escape Room it belongs to (leave blank if not applicable): ");

        roomDao.createRoom(new Room(theme, difficulty, escapeRoomId));
        System.out.println("Room created successfully!");
    }

    private void searchRoomById() throws SQLException {
        int idGet = scanner.readInt("ID of the Room to search: ");
        Optional<Room> optionalRoom = roomDao.getRoomById(idGet);
        if (optionalRoom.isPresent()) {
            System.out.println("Room found: " + optionalRoom.get());
        } else {
            System.out.println("Room with ID " + idGet + " not available.");
        }
    }

    private void listAllRooms() throws SQLException {
        List<Room> rooms = roomDao.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private void updateRoom() throws SQLException, RoomNotFoundException {
        int idUpdate = scanner.readInt("ID of the Room to be updated: ");
        Optional<Room> roomToUpdateOptional = roomDao.getRoomById(idUpdate);

        if (roomToUpdateOptional.isPresent()) {
            Room roomToUpdate = roomToUpdateOptional.get();
            String newTheme = scanner.readString("New theme (actual: " + roomToUpdate.getTheme() + "): ");
            int newDifficulty = scanner.readInt("New difficulty level (actual: " + roomToUpdate.getDifficultyLevel() + "): ");

            Integer newEscapeRoomId = scanner.readOptionalInt("New ID of the Escape Room (actual: "
                    + (roomToUpdate.getEscapeRoomId() != null ? roomToUpdate.getEscapeRoomId() : "NULL")
                    + ", leave blank if not applicable): ");

            roomToUpdate.setTheme(newTheme);
            roomToUpdate.setDifficultyLevel(newDifficulty);
            roomToUpdate.setEscapeRoomId(newEscapeRoomId);

            roomDao.updateRoom(roomToUpdate);
            System.out.println("Room updated successfully!");
        } else {
            System.out.println("Room with ID " + idUpdate + " not found to update.");
        }
    }

    private void deleteRoom() throws SQLException, RoomNotFoundException {
        int idDelete = scanner.readInt("ID of the Room to be deleted: ");
        roomDao.deleteRoom(idDelete);
        System.out.println("Room deleted successfully!");
    }
}
