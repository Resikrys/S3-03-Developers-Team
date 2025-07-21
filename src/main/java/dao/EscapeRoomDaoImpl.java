package dao;

import dbconnection.SQLExecutor;
import model.EscapeRoom;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import exception.EscapeRoomNotFoundException;

public class EscapeRoomDaoImpl implements EscapeRoomDAO {

    private final SQLExecutor sqlExecutor;

    public EscapeRoomDaoImpl() {
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void createEscaperoom(EscapeRoom escapeRoom) throws SQLException {
        String sql = "INSERT INTO EscapeRoom (name, total_tickets) VALUES (?, ?)";
        try {
            sqlExecutor.executeUpdate(
                    sql,
                    escapeRoom.getName(),
                    escapeRoom.getTotalTickets()
            );
        } catch (SQLException e) {
            System.err.println("Error creating EscapeRoom with name '" + escapeRoom.getName() + "': " + e.getMessage());
            e.printStackTrace(); // Para depuración
            throw e; // Relanzar la excepción
        }
    }

    @Override
    public List<EscapeRoom> getAll() throws SQLException {
        List<EscapeRoom> escapeRooms = new ArrayList<>();
        String sql = "SELECT id, name, total_tickets FROM EscapeRoom";
        try {
            sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            while (rs.next()) {
                                escapeRooms.add(new EscapeRoom(
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getInt("total_tickets")
                                ));
                            }
                        } catch (SQLException e) {
                            System.err.println("Error processing ResultSet for all EscapeRooms: " + e.getMessage());
                            e.printStackTrace(); // Para depuración
                        }
                        return null; // El valor de retorno del procesador no se usa para List<EscapeRoom>
                    }
            );
        } catch (SQLException e) {
            System.err.println("Error obtaining all EscapeRooms: " + e.getMessage());
            e.printStackTrace(); // Para depuración
            throw e; // Relanzar la excepción
        }
        return escapeRooms;
    }

    // Añadimos un método getById para EscapeRoom, ya que es fundamental para update/delete y tests
    public Optional<EscapeRoom> getEscapeRoomById(int id) throws SQLException {
        String sql = "SELECT id, name, total_tickets FROM EscapeRoom WHERE id = ?";
        try {
            return sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            if (rs.next()) {
                                return Optional.of(new EscapeRoom(
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getInt("total_tickets")
                                ));
                            }
                        } catch (SQLException e) {
                            System.err.println("Error processing ResultSet for EscapeRoom ID " + id + ": " + e.getMessage());
                            e.printStackTrace();
                            throw new RuntimeException("Error processing ResultSet for EscapeRoom", e);
                        }
                        return Optional.empty();
                    },
                    id
            );
        } catch (SQLException e) {
            System.err.println("Error getting EscapeRoom with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public void updateEscaperoom(EscapeRoom escapeRoom) throws SQLException, EscapeRoomNotFoundException {
        String sql = "UPDATE EscapeRoom SET name = ?, total_tickets = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    escapeRoom.getName(),
                    escapeRoom.getTotalTickets(),
                    escapeRoom.getId()
            );
            if (rowsAffected == 0) {
                // Si no se afectó ninguna fila, es porque la EscapeRoom no existe
                throw new EscapeRoomNotFoundException("EscapeRoom with ID " + escapeRoom.getId() + " not found for update.");
            }
        } catch (EscapeRoomNotFoundException e) {
            // Atrapa y relanza la excepción específica
            throw e;
        } catch (SQLException e) {
            // Atrapa cualquier otra SQLException (problemas técnicos de DB)
            System.err.println("Error updating EscapeRoom with ID " + escapeRoom.getId() + ": " + e.getMessage());
            e.printStackTrace(); // Para depuración
            throw e; // Relanzar la SQLException
        }
    }
    @Override
    public void deleteEscaperoom(int id) throws SQLException, EscapeRoomNotFoundException {
        String sql = "DELETE FROM EscapeRoom WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);
            if (rowsAffected == 0) {
                // Si no se afectó ninguna fila, es porque la EscapeRoom no existe
                throw new EscapeRoomNotFoundException("EscapeRoom with ID " + id + " not found for deletion.");
            }
        } catch (EscapeRoomNotFoundException e) {
            // Atrapa y relanza la excepción específica
            throw e;
        } catch (SQLException e) {
            // Atrapa cualquier otra SQLException
            System.err.println("Error deleting EscapeRoom with ID " + id + ": " + e.getMessage());
            e.printStackTrace(); // Para depuración
            throw e; // Relanzar la SQLException
        }
    }
}
