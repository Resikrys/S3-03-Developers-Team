package dao;

import model.Room;
import util.SQLExecutor;

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
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId()
            );
            if (rowsAffected > 0) {
                System.out.println("Room with theme '" + room.getTheme() + "' created.");
            }
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
                            throw new RuntimeException(e);
                        }
                        return Optional.empty();
                    },
                    id
            );
        } catch (SQLException e) {
            System.err.println("Error al obtener Room con ID " + id + ": " + e.getMessage());
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
                            e.printStackTrace();
                        }
                        return null;
                    }
            );
        } catch (SQLException e) {
            System.err.println("Error obtaining all Rooms: " + e.getMessage());
            throw e;
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Room SET theme = ?, difficulty_level = ?, escape_room_id = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId(),
                    room.getId()
            );
            if (rowsAffected > 0) {
                System.out.println("Room with ID " + room.getId() + " updated.");
            } else {
                System.out.println("Cannot find Room with ID " + room.getId() + " to update.");
            }
        } catch (SQLException e) {
            System.err.println("Error at updating Room with ID " + room.getId() + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteRoom(int id) throws SQLException {
        String sql = "DELETE FROM Room WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);
            if (rowsAffected > 0) {
                System.out.println("Room with ID " + id + " deleted.");
            } else {
                System.out.println("Cannot find Room with ID " + id + " to delete.");
            }
        } catch (SQLException e) {
            System.err.println("Error at deleting Room with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
}
