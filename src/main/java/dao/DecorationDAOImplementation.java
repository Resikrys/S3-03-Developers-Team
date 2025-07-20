package dao;

import enums.Material;
import model.DecorationObject;
import util.SQLExecutor;
import exception.DecorationNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DecorationDAOImplementation implements DecorationObjectDAO{

    private final SQLExecutor sqlExecutor;

    // Constructor que inicializa el ejecutor de SQL (gestiona la conexión y ejecución de sentencias)
    public DecorationDAOImplementation() {
        this.sqlExecutor = new SQLExecutor();
    }

    // Crea un objeto de decoración en la base de datos
    @Override
    public void createDecoration(DecorationObject decoration) throws SQLException {
        String sql = "INSERT INTO DecorationObject (name, price, material, room_id) VALUES (?, ?, ?, ?)";
        try {
            sqlExecutor.executeUpdate(
                    sql,
                    decoration.getName(),                    // nombre
                    decoration.getPrice(),                   // precio
                    decoration.getMaterial().toString(),     // material (enum convertido a String)
                    decoration.getRoomId()                   // ID de la sala
            );
        } catch (SQLException e) {
            System.err.println("Error al crear decoración: " + e.getMessage());
            throw e;
        }
    }

    // Recupera un objeto de decoración por su ID
    @Override
    public DecorationObject getDecorationById(int id) throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject WHERE id = ?";
        return sqlExecutor.executeQuery(sql, rs -> {
            try {
                if (rs.next()) {
                    // Si se encuentra, se construye y devuelve un objeto DecorationObject
                    return new DecorationObject(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            Material.valueOf(rs.getString("material")),
                            rs.getInt("room_id")
                    );
                }
            } catch (SQLException e) {
                System.err.println("Error al leer decoración por ID: " + e.getMessage());
            }
            return null; // Si no se encuentra, se devuelve null
        }, id);
    }

    // Recupera todas las decoraciones existentes en la base de datos
    @Override
    public List<DecorationObject> getAllDecorations() throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject";
        return sqlExecutor.executeQuery(sql, rs -> {
            List<DecorationObject> decorations = new ArrayList<>();
            try {
                while (rs.next()) {
                    // Agregamos cada decoración encontrada a la lista
                    decorations.add(new DecorationObject(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            Material.valueOf(rs.getString("material")),
                            rs.getInt("room_id")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Error al leer todas las decoraciones: " + e.getMessage());
            }
            return decorations;
        });
    }

    // Actualiza los datos de una decoración ya existente
    @Override
    public void updateDecoration(DecorationObject decoration) throws SQLException, DecorationNotFoundException {
        String sql = "UPDATE DecorationObject SET name = ?, price = ?, material = ?, room_id = ? WHERE id = ?";
        int rowsAffected = sqlExecutor.executeUpdate(
                sql,
                decoration.getName(),
                decoration.getPrice(),
                decoration.getMaterial().toString(),
                decoration.getRoomId(),
                decoration.getId()
        );

        // Si no se actualizó ninguna fila, lanzamos excepción personalizada
        if (rowsAffected == 0) {
            throw new DecorationNotFoundException("Decoración con ID " + decoration.getId() + " no encontrada para actualizar.");
        }
    }

    // Elimina una decoración por ID
    @Override
    public void deleteDecoration(int id) throws SQLException, DecorationNotFoundException {
        String sql = "DELETE FROM DecorationObject WHERE id = ?";
        int rowsAffected = sqlExecutor.executeUpdate(sql, id);

        // Si no se borró ninguna fila, lanzamos excepción personalizada
        if (rowsAffected == 0) {
            throw new DecorationNotFoundException("Decoración con ID " + id + " no encontrada para eliminar.");
        }
    }

    // Recupera todas las decoraciones asociadas a una sala concreta (room_id)
    @Override
    public List<DecorationObject> getDecorationsByRoomId(int roomId) throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject WHERE room_id = ?";
        return sqlExecutor.executeQuery(sql, rs -> {
            List<DecorationObject> decorations = new ArrayList<>();
            try {
                while (rs.next()) {
                    decorations.add(new DecorationObject(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            Material.valueOf(rs.getString("material")),
                            rs.getInt("room_id")
                    ));
                }
            } catch (SQLException e) {
                System.err.println("Error al leer decoraciones por Room ID: " + e.getMessage());
            }
            return decorations;
        }, roomId);
    }
}
