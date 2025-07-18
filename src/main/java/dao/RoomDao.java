package dao;

import model.Room;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoomDao {
    void createRoom(Room room) throws SQLException;
    Optional<Room> getRoomById(int id) throws SQLException;
    List<Room> getAllRooms() throws SQLException;
    void updateRoom(Room room) throws SQLException;
    void deleteRoom(int id) throws SQLException;
}