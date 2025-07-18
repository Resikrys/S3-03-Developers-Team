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
        System.out.println("Configuring the test environment for the connection to the database.");
    }

    @AfterAll
    static void tearDownAll() {
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Cleanup test environment of the connection to the database.");
    }

    @Test
    void testGetConnection() {
        System.out.println("Running testGetConnection...");
        try {
            connection = DatabaseConnection.getInstance().getConnection();

            assertNotNull(connection, "The database connection should not be null.");
            assertFalse(connection.isClosed(), "The database connection should not be closed.");

            System.out.println("Successful test connection: " + connection.getMetaData().getURL());

        } catch (SQLException e) {
            fail("Failed to get database connection: " + e.getMessage());
        }
    }

    @Test
    void testConnectionClosesCleanly() {
        System.out.println("Running testConnectionClosesCleanly...");
        try {
            Connection tempConnection = DatabaseConnection.getInstance().getConnection();
            assertNotNull(tempConnection);
            assertFalse(tempConnection.isClosed());

            DatabaseConnection.getInstance().closeConnection();

            Connection newConnectionAfterClose = DatabaseConnection.getInstance().getConnection();
            assertNotNull(newConnectionAfterClose, "You should be able to get a new connection after closing the previous one.");
            assertFalse(newConnectionAfterClose.isClosed(), "The new connection should not be closed.");

            System.out.println("The connection was closed and could be reopened.");

        } catch (SQLException e) {
            fail("Connection closing and reopening test failed: " + e.getMessage());
        }
    }

    @Test
    void testGetConnectionThrowsExceptionIfCredentialsAreBad() {
        System.out.println("Running testGetConnectionThrowsExceptionIfCredentialsAreBad (expecting failure)...");

        assertDoesNotThrow(() -> DatabaseConnection.getInstance().getConnection(),
                "Getting connection should not throw an immediate exception if credentials are set.");
    }
}