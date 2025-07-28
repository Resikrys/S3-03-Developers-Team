package dao;

import model.ClueObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClueDao {
    void createClue(ClueObject clueObject) throws SQLException;

    Optional<ClueObject> getClueById(int id) throws SQLException;

    List<ClueObject> getAllClues() throws SQLException;

    void updateClue(ClueObject clueObject) throws SQLException;

    void deleteClue(int id) throws SQLException;

    void markClueAsSolved(int id) throws SQLException;

    List<ClueObject> getCluesByRoomId(int roomId) throws SQLException;
}
