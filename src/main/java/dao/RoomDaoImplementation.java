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
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void createRoom(Room room) throws SQLException { // Cambiado a createRoom
        // Asumiendo que 'id' es AUTO_INCREMENT y no se incluye en el INSERT
        String sql = "INSERT INTO Room (theme, difficulty_level, escape_room_id) VALUES (?, ?, ?)";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId()
            );
            if (rowsAffected > 0) {
                System.out.println("Room con tema '" + room.getTheme() + "' creada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la Room con tema '" + room.getTheme() + "': " + e.getMessage());
            throw e; // Relanza la excepción para que el llamador la maneje
        }
    }

    @Override
    public Optional<Room> getRoomById(int id) throws SQLException {
        String sql = "SELECT id, theme, difficulty_level, escape_room_id FROM Room WHERE id = ?";
        try {
            // El lambda ahora devuelve directamente un Optional<Room>
            return sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            if (rs.next()) {
                                Integer escapeRoomId = (Integer) rs.getObject("escape_room_id"); // Usamos getObject para NULLs
                                return Optional.of(new Room(
                                        rs.getInt("id"),
                                        rs.getString("theme"),
                                        rs.getInt("difficulty_level"),
                                        escapeRoomId // Pasar Integer
                                ));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return Optional.empty(); // Si no hay resultados, retorna Optional.empty()
                    },
                    id
            );
        } catch (SQLException e) {
            System.err.println("Error al obtener Room con ID " + id + ": " + e.getMessage());
            throw e; // Relanza la excepción
        }
    }

    @Override
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT id, theme, difficulty_level, escape_room_id FROM Room";
        try {
            sqlExecutor.executeQuery(
                    sql,
                    rs -> {
                        try {
                            while (rs.next()) {
                                Integer escapeRoomId = (Integer) rs.getObject("escape_room_id"); // Leer como Integer
                                rooms.add(new Room(
                                        rs.getInt("id"),
                                        rs.getString("theme"),
                                        rs.getInt("difficulty_level"),
                                        escapeRoomId // Pasar Integer
                                ));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace(); // Manejo de error dentro del procesador si quieres
                        }
                        return null; // El valor de retorno del procesador no se usa para List<Room>
                    }
            );
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las Rooms: " + e.getMessage());
            throw e;
        }
        return rooms;
    }

    @Override
    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Room SET theme = ?, difficulty_level = ?, escape_room_id = ? WHERE id = ?";
        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    room.getTheme(),
                    room.getDifficultyLevel(),
                    room.getEscapeRoomId(), // Si es null, setObject lo manejará correctamente
                    room.getId()
            );
            if (rowsAffected > 0) {
                System.out.println("Room con ID " + room.getId() + " actualizada exitosamente.");
            } else {
                System.out.println("No se encontró la Room con ID " + room.getId() + " para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la Room con ID " + room.getId() + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteRoom(int id) throws SQLException {
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
            throw e;
        }
    }
}
