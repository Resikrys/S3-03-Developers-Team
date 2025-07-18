package dao;

import model.Room;
import util.SQLExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoImplementation implements RoomDao {
    private SQLExecutor sqlExecutor;

    public RoomDaoImplementation() {
        this.sqlExecutor = new SQLExecutor(); // SQLExecutor gestionará la conexión a través de DatabaseConnection Singleton
    }

    @Override
    public void createRoom(Room room) {
        String sql = "INSERT INTO rooms (theme, difficulty_level) VALUES (?, ?)";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficulty_level()
            );
            if (rowsAffected > 0) {
                System.out.println("Room '" + room.getId() + "' guardada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la Room '" + room.getId() + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Room> getRoomById(int id) {
        String sql = "SELECT id, theme, difficulty_level FROM rooms WHERE id = ?";
        try {
            return sqlExecutor.executeQuery(
                    sql,
                    rs -> { // Función para procesar el ResultSet
                        if (rs.next()) {
                            return Optional.of(new Room(
                                    rs.getInt("id"),
                                    rs.getString("theme"),
                                    rs.getInt("difficulty_level")
                            ));
                        }
                        return null;
                    },
                    id
            );
        } catch (SQLException e) {
            System.err.println("Error al obtener Room con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, theme, difficulty_level FROM rooms";
        try {
            sqlExecutor.executeQuery(
                    sql,
                    rs -> { // Función para procesar el ResultSet
                        try {
                            while (rs.next()) {
                                rooms.add(new Room(
                                        rs.getInt("id"),
                                        rs.getString("theme"),
                                        rs.getInt("difficulty_level")
                                ));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace(); // Manejo de error dentro del procesador
                        }
                        return null; // No necesitamos un retorno específico aquí, la lista se llena
                    }
            );
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las Rooms: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET  theme = ?, difficulty_level = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficulty_level(),
                    room.getId()
            );
            if (rowsAffected > 0) {
                System.out.println("Room con ID " + room.getId() + " actualizada exitosamente.");
            } else {
                System.out.println("No se encontró la Room con ID " + room.getId() + " para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la Room con ID " + room.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);
            if (rowsAffected > 0) {
                System.out.println("Room con ID " + id + " eliminada exitosamente.");
            } else {
                System.out.println("No se encontró la Room con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar Room con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
