package dao;

import enums.Material;
import model.ClueObject;
import util.SQLExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClueDAOImplementation implements ClueDao {

    private final SQLExecutor sqlExecutor;

    public ClueDAOImplementation(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public void createClue(ClueObject clueObject) throws SQLException {
        String sql = "INSERT INTO ClueObject (name, price, material, puzzle_description, solved, room_id) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            sqlExecutor.executeUpdate(
                    sql,
                    clueObject.getName(),
                    clueObject.getPrice(),
                    clueObject.getMaterial().toString(), // Enums se pasan como String
                    clueObject.getPuzzleDescription(),
                    clueObject.isSolved(),
                    clueObject.getRoomId()
            );
        } catch (SQLException e) {
            // Manejo de errores: muestra un mensaje en consola con el fallo
            System.err.println("❌ Error while creating clue '" + clueObject.getName() + "': " + e.getMessage());

            // Se relanza la excepción para que quien llamó al método pueda gestionarla si lo desea
            throw e;
        }
    }
    @Override
    public List<ClueObject> getAllClues() throws SQLException {
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject";
        // Ejecutamos la consulta usando una lambda que procesará el ResultSet
        return sqlExecutor.executeQuery(sql, rs -> {
            List<ClueObject> clues = new ArrayList<>();

            try {
                // Recorremos el ResultSet como de costumbre
                while (rs.next()) {
                    ClueObject clue = new ClueObject(
                            rs.getInt("id"),                                   // ID de la pista
                            rs.getString("name"),                              // Nombre
                            rs.getBigDecimal("price"),                         // Precio
                            Material.valueOf(rs.getString("material")),       // Enum del material
                            rs.getString("puzzle_description"),                // Descripción del puzzle
                            rs.getBoolean("solved"),                           // ¿Está resuelto?
                            rs.getInt("room_id")                               // ID de la habitación
                    );
                    clues.add(clue);  // Añadimos la pista a la lista
                }
            } catch (SQLException e) {
                // Si algo falla dentro de la lambda, imprimimos el error
                System.err.println("❌ Error while processing clues: " + e.getMessage());
            }

            return clues;  // Devolvemos la lista resultante
        });
    }

    @Override
    public ClueObject getClueById(int id) throws SQLException {
        // Consulta SQL para obtener una pista específica por su ID
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject WHERE id = ?";

        try {
            // Ejecutamos la consulta y pasamos cómo procesar el resultado
            return sqlExecutor.executeQuery(sql, rs -> {
                try {
                    // Si se encuentra un resultado en la base de datos
                    if (rs.next()) {
                        // Creamos un nuevo objeto ClueObject a partir de los datos de la base
                        return new ClueObject(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getBigDecimal("price"),
                                Material.valueOf(rs.getString("material")),
                                rs.getString("puzzle_description"),
                                rs.getBoolean("solved"),
                                rs.getInt("room_id")
                        );
                    }

                    // Si no hay ningún resultado, devolvemos null
                    return null;

                } catch (SQLException e) {
                    // Si hay un error leyendo los datos del ResultSet
                    System.err.println("❌ Error reading clue with id " + id + ": " + e.getMessage());
                    return null;
                }

            }, id); // Este es el valor que se usará en el '?' de la consulta

        } catch (SQLException e) {
            // Si hay un error ejecutando la consulta SQL
            System.err.println("❌ Error executing query for clue id " + id + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateClue(ClueObject clue) throws SQLException {
        // Sentencia SQL que actualizará una fila en la tabla ClueObject con los nuevos datos
        String sql = "UPDATE ClueObject SET name = ?, price = ?, material = ?, puzzle_description = ?, solved = ?, room_id = ? WHERE id = ?";

        try {
            // Ejecutamos la actualización usando SQLExecutor
            // Cada signo de interrogación (?) será reemplazado por uno de los valores de clue
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    clue.getName(),                    // Nuevo nombre de la pista
                    clue.getPrice(),                   // Nuevo precio
                    clue.getMaterial().name(),         // Nombre del material (convertido de enum a String)
                    clue.getPuzzleDescription(),       // Nueva descripción del acertijo
                    clue.isSolved(),                   // ¿Está resuelta la pista?
                    clue.getRoomId(),                  // ID de la habitación asociada
                    clue.getId()                       // ID de la pista que queremos actualizar (clave primaria)
            );

            // Si no se actualizó ninguna fila, significa que no existe ninguna pista con ese ID
            if (rowsAffected == 0) {
                System.err.println("⚠️ No clue found with id " + clue.getId() + " to update.");
            }

        } catch (SQLException e) {
            // Si hay un error al ejecutar la SQL, mostramos un mensaje por consola
            System.err.println("❌ Error updating clue with id " + clue.getId() + ": " + e.getMessage());
            throw e; // Re-lanzamos la excepción para que el código que llama este método también pueda manejarla
        }
    }

    @Override
    public void deleteClue(int id) throws SQLException {
        // Sentencia SQL para eliminar una pista por su ID
        String sql = "DELETE FROM ClueObject WHERE id = ?";

        try {
            // Ejecutamos el DELETE con el ID proporcionado
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);

            // Verificamos si realmente se eliminó algo
            if (rowsAffected == 0) {
                System.err.println("⚠️ No clue found with id " + id + " to delete.");
            } else {
                System.out.println("✅ Clue with id " + id + " successfully deleted.");
            }

        } catch (SQLException e) {
            // Si ocurre un error durante la eliminación, lo mostramos
            System.err.println("❌ Error deleting clue with id " + id + ": " + e.getMessage());
            throw e; // Re-lanzamos la excepción para que la maneje quien llame a este método
        }
    }
    @Override
    public void markClueAsSolved(int id) throws SQLException {
        // SQL que marca la pista como resuelta (solved = true) en la BBDD
        String sql = "UPDATE ClueObject SET solved = true WHERE id = ?";

        try {
            // Ejecutamos la sentencia pasando el ID como parámetro
            sqlExecutor.executeUpdate(sql, id);
        } catch (SQLException e) {
            // Si ocurre un error durante la actualización, lo mostramos en consola
            System.err.println("❌ Error marking clue with id " + id + " as solved: " + e.getMessage());
            throw e; // Re-lanzamos la excepción para que pueda ser manejada por quien llama
        }
    }
    @Override
    public List<ClueObject> getCluesByRoomId(int roomId) throws SQLException {
        // SQL que recupera todas las pistas asociadas a una sala específica
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject WHERE room_id = ?";

        // Ejecutamos la query usando una expresión lambda que procesa el ResultSet
        return sqlExecutor.executeQuery(sql, rs -> {
            List<ClueObject> clues = new ArrayList<>(); // Lista para guardar las pistas

            try {
                // Recorremos el ResultSet y construimos objetos ClueObject con cada fila
                while (rs.next()) {
                    clues.add(new ClueObject(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            Material.valueOf(rs.getString("material")),
                            rs.getString("puzzle_description"),
                            rs.getBoolean("solved"),
                            rs.getInt("room_id")
                    ));
                }
            } catch (SQLException e) {
                // Si ocurre un error al leer los datos, lo mostramos
                System.err.println("❌ Error reading clues for room_id " + roomId + ": " + e.getMessage());
            }

            return clues; // Devolvemos la lista construida
        }, roomId); // Se pasa el parámetro roomId al SQL
    }



}