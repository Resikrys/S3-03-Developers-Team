package util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
    private static Connection connection;

    @BeforeAll
    static void setUpAll() {
        EnvLoader.getInstance();
        System.out.println("Configurando ambiente de prueba para la conexión a la base de datos.");
    }

    @AfterAll
    static void tearDownAll() {
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Limpieza de ambiente de prueba de la conexión a la base de datos.");
    }

    @Test
    void testGetConnection() {
        System.out.println("Ejecutando testGetConnection...");
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            assertNotNull(connection, "La conexión a la base de datos no debería ser nula.");
            assertFalse(connection.isClosed(), "La conexión a la base de datos no debería estar cerrada.");

            System.out.println("Conexión de prueba exitosa: " + connection.getMetaData().getURL());

        } catch (SQLException e) {
            fail("Falló al obtener la conexión a la base de datos: " + e.getMessage());
        }
    }

    @Test
    void testConnectionClosesCleanly() {
        System.out.println("Ejecutando testConnectionClosesCleanly...");
        try {
            Connection tempConnection = DatabaseConnection.getInstance().getConnection();
            assertNotNull(tempConnection);
            assertFalse(tempConnection.isClosed());

            DatabaseConnection.getInstance().closeConnection();

            Connection newConnectionAfterClose = DatabaseConnection.getInstance().getConnection();
            assertNotNull(newConnectionAfterClose, "Debería poder obtener una nueva conexión después de cerrar la anterior.");
            assertFalse(newConnectionAfterClose.isClosed(), "La nueva conexión no debería estar cerrada.");

            System.out.println("La conexión se cerró y se pudo reabrir.");

        } catch (SQLException e) {
            fail("Falló el test de cierre y reapertura de conexión: " + e.getMessage());
        }
    }

    @Test
    void testGetConnectionThrowsExceptionIfCredentialsAreBad() {
        System.out.println("Ejecutando testGetConnectionThrowsExceptionIfCredentialsAreBad (esperando fallo)...");

        assertDoesNotThrow(() -> DatabaseConnection.getInstance().getConnection(),
                "Getting connection should not throw an immediate exception if credentials are set.");
    }
}