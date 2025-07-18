package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance; // La única instancia de la clase
    private Connection connection; // La conexión a la base de datos

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    // Constructor privado para evitar instanciación externa
    private DatabaseConnection() {
        // Utiliza EnvLoader para obtener las credenciales
        EnvLoader envLoader = EnvLoader.getInstance();

        String dbHost = envLoader.getEnv("MYSQL_HOST", "localhost");
        String dbPort = envLoader.getEnv("MYSQL_PORT", "3307");
        String dbName = envLoader.getEnv("MYSQL_DATABASE", "escaperoom_db");

        this.dbUrl = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
        this.dbUser = envLoader.getEnv("MYSQL_USER", "escaperoom_app_user");
        this.dbPassword = envLoader.getEnv("MYSQL_PASSWORD"); // Sin valor por defecto aquí por seguridad

        // Validar que la contraseña no sea nula
        if (this.dbPassword == null || this.dbPassword.isEmpty()) {
            throw new RuntimeException("Error: La contraseña de la base de datos (MYSQL_PASSWORD) no está definida en el archivo .env o en el entorno.");
        }

        // Cargar el driver JDBC una sola vez
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver no encontrado. Asegúrate de que la dependencia esté en tu pom.xml.");
            throw new RuntimeException("Fallo al cargar el driver JDBC", e);
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
                // System.out.println("DEBUG - Abriendo nueva conexión a DB URL: " + dbUrl); // Para depuración
                // System.out.println("DEBUG - DB User: " + dbUser); // Para depuración
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } catch (SQLException e) {
                System.err.println("Error al obtener la conexión a la base de datos: " + e.getMessage());
                throw e; // Relanzar la excepción
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}
