package manager;

import dao.RoomDao;
import dao.RoomDaoImplementation;
import exception.InvalidInputException;
import exception.RoomNotFoundException;
import model.Room;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoomManager {
    private final RoomDao roomDao;
    private final InputHelper inputHelper;

    public RoomManager(InputHelper inputHelper) {
        this.roomDao = new RoomDaoImplementation();
        this.inputHelper = inputHelper;
    }

    public void createRoom() throws SQLException {
        String theme = inputHelper.readString("Theme: ");
        int difficulty = inputHelper.readInt("Difficulty level (1-10): ");
        Integer escapeRoomId = inputHelper.readOptionalInt("ID of the Escape Room it belongs to (leave blank if not applicable): ");

        roomDao.createRoom(new Room(theme, difficulty, escapeRoomId));
        System.out.println("Room created successfully!");
    }

    public void searchRoomById() throws SQLException {
        int idGet = inputHelper.readInt("ID of the Room to search: ");
        Optional<Room> optionalRoom = roomDao.getRoomById(idGet);
        if (optionalRoom.isPresent()) {
            System.out.println("Room found: " + optionalRoom.get());
        } else {
            System.out.println("Room with ID " + idGet + " not available.");
        }
    }

    public void listAllRooms() throws SQLException {
        List<Room> rooms = roomDao.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    public void updateRoom() throws SQLException, RoomNotFoundException {
        int idUpdate = inputHelper.readInt("ID of the Room to be updated: ");
        Optional<Room> roomToUpdateOptional = roomDao.getRoomById(idUpdate);

        if (roomToUpdateOptional.isPresent()) {
            Room roomToUpdate = roomToUpdateOptional.get();
            String newTheme = inputHelper.readString("New theme (actual: " + roomToUpdate.getTheme() + "): ");
            int newDifficulty = inputHelper.readInt("New difficulty level (actual: " + roomToUpdate.getDifficultyLevel() + "): ");

            Integer newEscapeRoomId = inputHelper.readOptionalInt("New ID of the Escape Room (actual: "
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

    public void deleteRoom() throws SQLException, RoomNotFoundException {
        int idDelete = inputHelper.readInt("ID of the Room to be deleted: ");
        roomDao.deleteRoom(idDelete);
        System.out.println("Room deleted successfully!");
    }
}
