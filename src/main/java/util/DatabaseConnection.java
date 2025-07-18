package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private DatabaseConnection() {
        EnvLoader envLoader = EnvLoader.getInstance();

        String dbHost = envLoader.getEnv("MYSQL_HOST", "localhost");
        String dbPort = envLoader.getEnv("MYSQL_PORT", "3307");
        String dbName = envLoader.getEnv("MYSQL_DATABASE", "escaperoom_db");

        this.dbUrl = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
        this.dbUser = envLoader.getEnv("MYSQL_USER", "escaperoom_app_user");
        this.dbPassword = envLoader.getEnv("MYSQL_PASSWORD");

        if (this.dbPassword == null || this.dbPassword.isEmpty()) {
            throw new RuntimeException("Error: The database password (MYSQL_PASSWORD) is not defined in the .env file or in the environment.");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found. Make sure the dependency is in your pom.xml.");
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } catch (SQLException e) {
                System.err.println("Error getting database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
