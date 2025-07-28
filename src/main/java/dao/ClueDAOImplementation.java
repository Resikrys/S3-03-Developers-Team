package dao;

import enums.Material;
import exception.NotFoundException;
import model.ClueObject;
import dbconnection.SQLExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClueDAOImplementation implements ClueDao {

    private final SQLExecutor sqlExecutor;

    public ClueDAOImplementation() { // Modified constructor for direct instantiation
        this.sqlExecutor = new SQLExecutor();
    }

    @Override
    public void createClue(ClueObject clueObject) throws SQLException {
        String sql = "INSERT INTO ClueObject (name, price, material, puzzle_description, solved, room_id) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            sqlExecutor.executeUpdate(
                    sql,
                    clueObject.getName(),
                    clueObject.getPrice(),
                    clueObject.getMaterial().toString(),
                    clueObject.getPuzzleDescription(),
                    clueObject.isSolved(),
                    clueObject.getRoomId()
            );
            System.out.println("✅ Clue '" + clueObject.getName() + "' created successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Error while creating clue '" + clueObject.getName() + "': " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ClueObject> getAllClues() throws SQLException {
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject";
        try {
            return sqlExecutor.executeQuery(sql, rs -> {
                List<ClueObject> clues = new ArrayList<>();
                try {
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
                    System.err.println("❌ Error while processing clues: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ClueObject ResultSet", e);
                }
                return clues;
            });
        } catch (SQLException e) {
            System.err.println("❌ Error obtaining all clues: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Optional<ClueObject> getClueById(int id) throws SQLException {
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject WHERE id = ?";

        try {
            return sqlExecutor.executeQuery(sql, rs -> {
                try {
                    if (rs.next()) {
                        return Optional.of(new ClueObject(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getBigDecimal("price"),
                                Material.valueOf(rs.getString("material")),
                                rs.getString("puzzle_description"),
                                rs.getBoolean("solved"),
                                rs.getInt("room_id")
                        ));
                    }
                    return Optional.empty();
                } catch (SQLException e) {
                    System.err.println("❌ Error reading clue with id " + id + " from ResultSet: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ClueObject ResultSet for ID " + id, e);
                }
            }, id);
        } catch (SQLException e) {
            System.err.println("❌ Error executing query for clue id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void updateClue(ClueObject clue) throws SQLException, NotFoundException {
        String sql = "UPDATE ClueObject SET name = ?, price = ?, material = ?, puzzle_description = ?, solved = ?, room_id = ? WHERE id = ?";

        try {
            int rowsAffected = sqlExecutor.executeUpdate(
                    sql,
                    clue.getName(),
                    clue.getPrice(),
                    clue.getMaterial().name(),
                    clue.getPuzzleDescription(),
                    clue.isSolved(),
                    clue.getRoomId(),
                    clue.getId()
            );

            if (rowsAffected == 0) {
                throw new NotFoundException("Clue with id " + clue.getId() + " not found for update.");
            }
            System.out.println("✅ Clue with id " + clue.getId() + " updated successfully.");

        } catch (NotFoundException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("❌ Error updating clue with id " + clue.getId() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteClue(int id) throws SQLException, NotFoundException {
        String sql = "DELETE FROM ClueObject WHERE id = ?";

        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);

            if (rowsAffected == 0) {
                throw new NotFoundException("Clue with id " + id + " not found for deletion.");
            }
            System.out.println("✅ Clue with id " + id + " successfully deleted.");

        } catch (NotFoundException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("❌ Error deleting clue with id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void markClueAsSolved(int id) throws SQLException, NotFoundException {
        String sql = "UPDATE ClueObject SET solved = true WHERE id = ?";

        try {
            int rowsAffected = sqlExecutor.executeUpdate(sql, id);
            if (rowsAffected == 0) {
                throw new NotFoundException("Clue with id " + id + " not found to mark as solved.");
            }
            System.out.println("✅ Clue with id " + id + " marked as solved.");
        } catch (NotFoundException e) {
            throw e;
        } catch (SQLException e) {
            System.err.println("❌ Error marking clue with id " + id + " as solved: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<ClueObject> getCluesByRoomId(int roomId) throws SQLException {
        String sql = "SELECT id, name, price, material, puzzle_description, solved, room_id FROM ClueObject WHERE room_id = ?";

        try {
            return sqlExecutor.executeQuery(sql, rs -> {
                List<ClueObject> clues = new ArrayList<>();
                try {
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
                    System.err.println("❌ Error reading clues for room_id " + roomId + " from ResultSet: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error processing ClueObject ResultSet for Room ID " + roomId, e);
                }
                return clues;
            }, roomId);
        } catch (SQLException e) {
            System.err.println("❌ Error obtaining clues for room ID " + roomId + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}