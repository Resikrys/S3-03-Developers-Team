package dao;

import enums.Material;
import model.DecorationObject;
import dbconnection.SQLExecutor;
import exception.DecorationNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DecorationDAOImplementation implements DecorationObjectDAO {

    private final SQLExecutor sqlExecutor;

    public DecorationDAOImplementation() {
        this.sqlExecutor = new SQLExecutor(); // Assumes SQLExecutor is instantiated directly
    }

    @Override
    public void createDecoration(DecorationObject decoration) throws SQLException {
        String sql = "INSERT INTO DecorationObject (name, price, material, room_id) VALUES (?, ?, ?, ?)";
        try {
            sqlExecutor.executeUpdate(
                    sql,
                    decoration.getName(),
                    decoration.getPrice(),
                    decoration.getMaterial().toString(),
                    decoration.getRoomId()
            );
            System.out.println("✅ Decoration '" + decoration.getName() + "' created successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Error al crear decoración '" + decoration.getName() + "': " + e.getMessage());
            e.printStackTrace(); // For debugging
            throw e; // Re-throw the exception
        }
    }

    @Override
    public Optional<DecorationObject> getDecorationById(int id) throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject WHERE id = ?";
        try {
            return sqlExecutor.executeQuery(sql, rs -> {
                try {
                    if (rs.next()) {
                        return Optional.of(new DecorationObject(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getBigDecimal("price"),
                                Material.valueOf(rs.getString("material")),
                                rs.getInt("room_id")
                        ));
                    }
                    return Optional.empty(); // If not found, return empty Optional
                } catch (SQLException e) {
                    System.err.println("❌ Error al leer decoración por ID " + id + " del ResultSet: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ResultSet for DecorationObject ID " + id, e); // Wrap and re-throw
                }
            }, id);
        } catch (SQLException e) {
            System.err.println("❌ Error al ejecutar consulta para decoración ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the SQLException from executeQuery
        }
    }

    @Override
    public List<DecorationObject> getAllDecorations() throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject";
        try {
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
                    System.err.println("❌ Error al leer todas las decoraciones del ResultSet: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ResultSet for all DecorationObjects", e); // Wrap and re-throw
                }
                return decorations;
            });
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener todas las decoraciones: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the SQLException from executeQuery
        }
    }

    @Override
    public void updateDecoration(DecorationObject decoration) throws SQLException, DecorationNotFoundException {
        String sql = "UPDATE DecorationObject SET name = ?, price = ?, material = ?, room_id = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    decoration.getName(),
                    decoration.getPrice(),
                    decoration.getMaterial().toString(),
                    decoration.getRoomId(),
                    decoration.getId()
            );

            if (rowsAffected == 0) {
                throw new DecorationNotFoundException("Decoración con ID " + decoration.getId() + " no encontrada para actualizar.");
            }
            System.out.println("✅ Decoration with ID " + decoration.getId() + " updated successfully.");
        } catch (DecorationNotFoundException e) {
            throw e; // Re-throw specific exception
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar decoración con ID " + decoration.getId() + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw other SQLExceptions
        }
    }

    @Override
    public void deleteDecoration(int id) throws SQLException, DecorationNotFoundException {
        String sql = "DELETE FROM DecorationObject WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);

            if (rowsAffected == 0) {
                throw new DecorationNotFoundException("Decoración con ID " + id + " no encontrada para eliminar.");
            }
            System.out.println("🗑️ Decoration with ID " + id + " deleted successfully.");
        } catch (DecorationNotFoundException e) {
            throw e; // Re-throw specific exception
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar decoración con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw other SQLExceptions
        }
    }

    @Override
    public List<DecorationObject> getDecorationsByRoomId(int roomId) throws SQLException {
        String sql = "SELECT id, name, price, material, room_id FROM DecorationObject WHERE room_id = ?";
        try {
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
                    System.err.println("❌ Error al leer decoraciones por Room ID " + roomId + " del ResultSet: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ResultSet for Decorations by Room ID " + roomId, e); // Wrap and re-throw
                }
                return decorations;
            }, roomId);
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener decoraciones para Room ID " + roomId + ": " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw the SQLException from executeQuery
        }
    }
}
