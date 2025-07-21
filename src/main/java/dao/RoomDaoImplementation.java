package dao;

import exception.RoomNotFoundException;
import model.Room;
import dbconnection.SQLExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoImplementation implements RoomDao {

    private SQLExecutor sqlExecutor;

    public RoomDaoImplementation() {
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void createRoom(Room room) throws SQLException {
        String sql = "INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES (?, ?, ?)";
        try {
            sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId()
            );
        } catch (SQLException e) {
            System.err.println("Error at creation Room with theme '" + room.getTheme() + "': " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Room> getRoomById(int id) throws SQLException {
        String sql = "SELECT id, theme, difficulty_level, escape_room_id FROM Room WHERE id = ?";
        try {
            return sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            if (rs.next()) {
                                Integer escapeRoomId = (Integer) rs.getObject("escape_room_id");
                                return Optional.of(new Room(
                                        rs.getInt("id"),
                                        rs.getString("theme"),
                                        rs.getInt("difficulty_level"),
                                        escapeRoomId
                                ));
                            }
                        } catch (SQLException e) {
                            System.err.println("Error processing ResultSet for Room ID " + id + ": " + e.getMessage());
                            e.printStackTrace();
                            throw new RuntimeException("Error processing ResultSet for Room", e);
                        }
                        return Optional.empty();
                    },
                    id
            );
        } catch (SQLException e) {
            System.err.println("Error getting Room with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, theme, difficulty_level, escape_room_id FROM Room";
        try {
            sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            while (rs.next()) {
                                Integer escapeRoomId = (Integer) rs.getObject("escape_room_id");
                                rooms.add(new Room(
                                        rs.getInt("id"),
                                        rs.getString("theme"),
                                        rs.getInt("difficulty_level"),
                                        escapeRoomId
                                ));
                            }
                        } catch (SQLException e) {
                            System.err.println("Error processing ResultSet for all Rooms: " + e.getMessage());
                            e.printStackTrace();
                        }
                        return null;
                    }
            );
        } catch (SQLException e) {
            System.err.println("Error obtaining all Rooms: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) throws SQLException, RoomNotFoundException {
        String sql = "UPDATE Room SET theme = ?, difficulty_level = ?, escape_room_id = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId(),
                    room.getId()
            );
            if (rowsAffected == 0) {
                throw new RoomNotFoundException("Room with ID " + room.getId() + " not found for update.");
            }
        } catch (RoomNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error updating Room with ID " + room.getId() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteRoom(int id) throws SQLException, RoomNotFoundException {
        String sql = "DELETE FROM Room WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);
            if (rowsAffected == 0) {
                throw new RoomNotFoundException("Room with ID " + id + " not found for deletion.");
            }
        } catch (RoomNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("Error deleting Room with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
