import java.sql.Connection;
import java.sql.DriverManager;

import menu.EscapeRoomMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Escape Room Manager ===");
        new EscapeRoomMenu().show();
    }
}





        /*String jdbcUrl = "jdbc:mysql://localhost:3308/escaperoom_db"; // PUERTO CORRECTO: 3307, DB: escaperoom_db_trial
        String dbUser = "escaperoom_user";                             // Usuario de tu .env
        String dbPassword = "root123";                   // Contraseña de tu .env

        System.out.println("Intentando conectar a la base de datos...");
        System.out.println("URL: " + jdbcUrl);
        System.out.println("Usuario: " + dbUser);
        // NO imprimas la contraseña en un entorno real.

       // RoomDao roomDao = null; // Declaramos el DAO fuera del try para que sea accesible

        try {
            // (Opcional) Pequeña prueba de conexión directa antes de usar el DAO para depuración
            try (Connection testConnection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
                System.out.println("¡Conexión de prueba directa a la base de datos exitosa!");
            }

        } catch (Exception e) {
            System.err.println("Error de conexión o de base de datos en la aplicación: " + e.getMessage());
            e.printStackTrace();

        }*/
   /* }
}*/