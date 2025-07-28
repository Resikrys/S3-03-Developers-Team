package dao;

import exception.NotFoundException;
import model.DecorationObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DecorationObjectDAO {
    void createDecoration(DecorationObject decoration) throws SQLException;

    Optional<DecorationObject> getDecorationById(int id) throws SQLException;

    List<DecorationObject> getAllDecorations() throws SQLException;

    void updateDecoration(DecorationObject decoration) throws SQLException, NotFoundException;

    void deleteDecoration(int id) throws SQLException, NotFoundException;

    List<DecorationObject> getDecorationsByRoomId(int roomId) throws SQLException;
}
