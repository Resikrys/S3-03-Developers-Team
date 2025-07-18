package util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private static EnvLoader instance; // La única instancia de la clase
    private final Dotenv dotenv;

    private EnvLoader() {
        try {
            this.dotenv = Dotenv.configure()
                    .filename(".env") // Nombre del archivo, por defecto es .env
                    .load();
        } catch (io.github.cdimascio.dotenv.DotenvException e) { // Mantener la excepción específica si la tienes definida en pom.xml
            System.err.println("Error: No se pudo cargar el archivo .env. Asegúrate de que exista en la raíz del proyecto o en la ruta especificada. " + e.getMessage());
            throw new RuntimeException("Fallo al cargar variables de entorno desde .env", e);
        } catch (Exception e) { // Capturar cualquier otra excepción inesperada
            System.err.println("Error inesperado al cargar el archivo .env: " + e.getMessage());
            throw new RuntimeException("Fallo inesperado al cargar variables de entorno desde .env", e);
        }
    }

    public static synchronized EnvLoader getInstance() {
        if (instance == null) {
            instance = new EnvLoader();
        }
        return instance;
    }

    public String getEnv(String key) {
        return dotenv.get(key);
    }

    public String getEnv(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }

}
