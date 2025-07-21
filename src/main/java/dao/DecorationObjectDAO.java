package dao;

import exception.NotFoundException;
import model.DecorationObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DecorationObjectDAO {
    // Crear un nuevo objeto de decoración
    void createDecoration(DecorationObject decoration) throws SQLException;

    // Obtener un objeto de decoración por su ID
    Optional<DecorationObject> getDecorationById(int id) throws SQLException;

    // Obtener todos los objetos de decoración
    List<DecorationObject> getAllDecorations() throws SQLException;

    // Actualizar un objeto de decoración existente
    void updateDecoration(DecorationObject decoration) throws SQLException, NotFoundException;

    // Eliminar un objeto de decoración por su ID
    void deleteDecoration(int id) throws SQLException, NotFoundException;

    // Obtener todos los objetos de una sala
    List<DecorationObject> getDecorationsByRoomId(int roomId) throws SQLException;
}
