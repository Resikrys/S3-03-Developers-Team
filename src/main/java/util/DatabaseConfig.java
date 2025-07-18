package util;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private final Dotenv dotenv;

    public DatabaseConfig() {
        this.dotenv = Dotenv.configure()
//              .directory("src/main/java/util/.env"") // Relative route to .env
                .directory("C:\\Users\\elyri\\IdeaProjects\\S3_03_Scaperoom")
                .filename(".env")     // Por defecto es ".env", puedes omitir esta línea
                .load();
    }

    public String getDbUrl() {
        String dbHost = dotenv.get("MYSQL_HOST", "localhost");
        String dbPort = dotenv.get("MYSQL_PORT", "3307");
        String dbName = dotenv.get("MYSQL_DATABASE", "escaperoom_db_trial");

        return String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
    }

    public String getDbUser() {
        return dotenv.get("MYSQL_USER", "escaperoom_app_user");
    }

    public String getDbPassword() {
        return dotenv.get("MYSQL_PASSWORD");
    }

    public Connection getConnection() throws SQLException {
        String url = getDbUrl();
        String user = getDbUser();
        String password = getDbPassword();

        if (password == null || password.isEmpty()) {
            throw new SQLException("Error: The database password (MYSQL_PASSWORD) is not defined in the .env file.");
        }

        //Cambiar por testing
//        System.out.println("DEBUG - Trying to connect to DB URL: " + url);
//        System.out.println("DEBUG - DB User: " + user);

        return DriverManager.getConnection(url, user, password);
    }
}
