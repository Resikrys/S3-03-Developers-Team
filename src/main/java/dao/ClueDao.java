package dao;

import model.ClueObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClueDao {
    // Crear una nueva pista
    void createClue(ClueObject clueObject) throws SQLException;

    // Obtener una pista por su ID
    Optional<ClueObject> getClueById(int id) throws SQLException;

    // Obtener todas las pistas
    List<ClueObject> getAllClues() throws SQLException;

    // Actualizar una pista existente
    void updateClue(ClueObject clueObject) throws SQLException;

    // Eliminar una pista por su ID
    void deleteClue(int id) throws SQLException;

    // Marcar una pista como resuelta
    void markClueAsSolved(int id) throws SQLException;

    // Obtener todas las pistas de una sala
    List<ClueObject> getCluesByRoomId(int roomId) throws SQLException;
}
