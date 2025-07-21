package dbconnection;

import java.sql.*;
import java.util.function.Function;

public class SQLExecutor {
    private DatabaseConnection dbConnection;

    public SQLExecutor() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }

    public <T> T executeQuery(String sql, Function<ResultSet, T> processor, Object... params) throws SQLException {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return processor.apply(rs);
            }
        }
    }

    public void executeDDL(String sql) throws SQLException {
        try (Connection connection = dbConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
