package util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private static EnvLoader instance;
    private final Dotenv dotenv;

    private EnvLoader() {
        try {
            this.dotenv = Dotenv.configure()
                    .filename(".env")
                    .load();
        } catch (io.github.cdimascio.dotenv.DotenvException e) {
            System.err.println("Error: The .env file could not be loaded. Make sure it exists in the project root or in the specified path. "
                    + e.getMessage());
            throw new RuntimeException("Failed to load environment variables from .env", e);
        } catch (Exception e) { // Capturar cualquier otra excepción inesperada
            System.err.println("Unexpected error loading .env file: " + e.getMessage());
            throw new RuntimeException("Unexpected failure loading environment variables from .env", e);
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
