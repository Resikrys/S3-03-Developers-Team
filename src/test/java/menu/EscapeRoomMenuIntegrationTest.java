
package menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.InputHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class EscapeRoomMenuIntegrationTest {

    private EscapeRoomMenu escapeRoomMenu;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Capturar System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Configurar entrada simulada
        String input = "1\nTest Room\n50\n0\n"; // opción 1, datos, salir
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputHelper inputHelper = new InputHelper();
        escapeRoomMenu = new EscapeRoomMenu(inputHelper);
    }

    @Test
    void showMenu_Option1_CreatesEscapeRoom() {
        // Act
        escapeRoomMenu.showMenu();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Escape Room"));
        assertTrue(output.contains("created successfully"));
    }

    @Test
    void showMenu_Option0_ExitsMenu() {
        // Arrange
        String input = "0\n"; // Solo salir
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputHelper inputHelper = new InputHelper();
        EscapeRoomMenu menu = new EscapeRoomMenu(inputHelper);

        // Act
        menu.showMenu();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Main Menu"));
    }

    @Test
    void showMenu_InvalidOption_ShowsErrorMessage() {
        // Arrange
        String input = "999\n0\n"; // opción inválida, luego salir
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputHelper inputHelper = new InputHelper();
        EscapeRoomMenu menu = new EscapeRoomMenu(inputHelper);

        // Act
        menu.showMenu();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid option"));
    }

    @Test
    void showMenu_WithDatabaseError_HandlesGracefully() {
        // Arrange - Simular error de base de datos
        String input = "1\nTest Room\n-50\n0\n"; // datos inválidos
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        InputHelper inputHelper = new InputHelper();
        EscapeRoomMenu menu = new EscapeRoomMenu(inputHelper);

        // Act
        menu.showMenu();

        // Assert
        String output = outputStream.toString();
        // Verificar que maneja el error sin crashear
        assertTrue(output.contains("Database error") || output.contains("Invalid"));
    }
}