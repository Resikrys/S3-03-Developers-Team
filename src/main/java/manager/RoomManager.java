package manager;

import dao.PlayerDao;
import dao.RoomDao;
import dao.RoomDaoImplementation;
import exception.InvalidInputException;
import exception.RoomNotFoundException;
import model.Room;
import manager.NotificationManager;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoomManager {
    private final RoomDao roomDao;
    private final PlayerDao playerDao;
    private final InputHelper inputHelper;
    private final NotificationManager notificationManager;

    // Constructor principal que inyecta PlayerDao y NotificationManager
    public RoomManager(InputHelper inputHelper, PlayerDao playerDao, NotificationManager notificationManager) {
        this.roomDao = new RoomDaoImplementation();
        this.inputHelper = inputHelper;
        this.playerDao = playerDao;
        this.notificationManager = notificationManager;
    }

    // Crear una nueva sala y notificar a los jugadores registrados
    public void createRoom() throws SQLException {
        String theme = inputHelper.readString("Theme: ");
        int difficulty = inputHelper.readInt("Difficulty level (1-10): ");
        Integer escapeRoomId = inputHelper.readOptionalInt("ID of the Escape Room it belongs to (leave blank if not applicable): ");

        Room newRoom = new Room(theme, difficulty, escapeRoomId);
        roomDao.createRoom(newRoom);
        System.out.println("✅ Room created successfully!");

        // Notificar a todos los jugadores registrados
        notificationManager.notifyAll("📢 A new room has been created: " + theme);
    }

    // Buscar sala por ID
    public void searchRoomById() throws SQLException {
        int id = inputHelper.readInt("ID of the Room to search: ");
        Optional<Room> optionalRoom = roomDao.getRoomById(id);
        if (optionalRoom.isPresent()) {
            System.out.println("🎯 Room found: " + optionalRoom.get());
        } else {
            System.out.println("❌ No room found with ID " + id);
        }
    }

    // Listar todas las salas
    public void listAllRooms() throws SQLException {
        List<Room> rooms = roomDao.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("📭 No rooms available.");
        } else {
            System.out.println("✅ List of all rooms:");
            rooms.forEach(System.out::println);
        }
    }

    // Actualizar datos de una sala
    public void updateRoom() throws SQLException, RoomNotFoundException {
        int id = inputHelper.readInt("ID of the Room to be updated: ");
        Optional<Room> optionalRoom = roomDao.getRoomById(id);

        if (optionalRoom.isPresent()) {
            Room roomToUpdate = optionalRoom.get();
            String newTheme = inputHelper.readString("New theme (current: " + roomToUpdate.getTheme() + "): ");
            int newDifficulty = inputHelper.readInt("New difficulty (current: " + roomToUpdate.getDifficultyLevel() + "): ");
            Integer newEscapeRoomId = inputHelper.readOptionalInt("New Escape Room ID (current: "
                    + (roomToUpdate.getEscapeRoomId() != null ? roomToUpdate.getEscapeRoomId() : "NULL") + "): ");

            roomToUpdate.setTheme(newTheme);
            roomToUpdate.setDifficultyLevel(newDifficulty);
            roomToUpdate.setEscapeRoomId(newEscapeRoomId);

            roomDao.updateRoom(roomToUpdate);
            System.out.println("✅ Room updated successfully!");
        } else {
            System.out.println("❌ No room found with ID " + id);
        }
    }

    // Eliminar sala por ID
    public void deleteRoom() throws SQLException, RoomNotFoundException {
        int id = inputHelper.readInt("ID of the Room to delete: ");
        roomDao.deleteRoom(id);
        System.out.println("🗑️ Room deleted successfully!");
    }
}