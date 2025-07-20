package dao;

import model.DecorationObject;

import java.sql.SQLException;
import java.util.List;

public interface DecorationObjectDAO {
    // Crear un nuevo objeto de decoración
    void createDecoration(DecorationObject decoration) throws SQLException;

    // Obtener un objeto de decoración por su ID
    DecorationObject getDecorationById(int id) throws SQLException;

    // Obtener todos los objetos de decoración
    List<DecorationObject> getAllDecorations() throws SQLException;

    // Actualizar un objeto de decoración existente
    void updateDecoration(DecorationObject decoration) throws SQLException;

    // Eliminar un objeto de decoración por su ID
    void deleteDecoration(int id) throws SQLException;

    // Obtener todos los objetos de una sala
    List<DecorationObject> getDecorationsByRoomId(int roomId) throws SQLException;
}
