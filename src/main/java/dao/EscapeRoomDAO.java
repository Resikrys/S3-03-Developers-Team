package dao;

import exception.EscapeRoomNotFoundException;
import model.EscapeRoom;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EscapeRoomDAO {
    void createEscaperoom(EscapeRoom escapeRoom) throws SQLException;
    List<EscapeRoom> getAll() throws SQLException;
    Optional<EscapeRoom> getEscapeRoomById(int id) throws SQLException;
    void updateEscaperoom(EscapeRoom escapeRoom) throws SQLException, EscapeRoomNotFoundException;
    void deleteEscaperoom(int id) throws SQLException, EscapeRoomNotFoundException;
}
