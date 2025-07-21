package menu;

import exception.EscapeRoomNotFoundException;
import exception.InvalidInputException;
import manager.EscapeRoomManager;
import util.InputHelper;

import java.sql.SQLException;

public class EscapeRoomMenu {
    private final InputHelper inputHelper;
    private final EscapeRoomManager escaperoomManager;

    public EscapeRoomMenu(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
        this.escaperoomManager = new EscapeRoomManager(inputHelper); // RoomManager still needs InputHelper
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n--- CRUD for Escape Rooms ---");
            System.out.println("1. Create Escape Room");
            System.out.println("2. Search Escape Room by ID");
            System.out.println("3. List all Escape Rooms");
            System.out.println("4. Update Escape Room");
            System.out.println("5. Delete Escape Room");
            System.out.println("0. Back to Main Menu");
            choice = inputHelper.readInt("Select a valid option for Escape Rooms Menu: ");

            try {
                switch (choice) {
                    case 1:
                        escaperoomManager.createEscapeRoom();
                        break;
                    case 2:
                        escaperoomManager.searchEscapeRoomById();
                        break;
                    case 3:
                        escaperoomManager.listAllEscapeRooms();
                        break;
                    case 4:
                        escaperoomManager.updateEscapeRoom();
                        break;
                    case 5:
                        escaperoomManager.deleteEscapeRoom();
                        break;
                    case 0:
                        System.out.println("Returning to Main Menu.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InvalidInputException e) {
                // Atrapa errores de formato de entrada del usuario (ej. texto donde se espera número)
                System.err.println("Input error: " + e.getMessage() + ". Please try again.");
            } catch (EscapeRoomNotFoundException e) {
                // Atrapa errores lógicos de negocio (ej. no se encuentra la entidad)
                System.err.println("Operation error: " + e.getMessage());
            } catch (SQLException e) {
                // Atrapa errores técnicos de la base de datos
                System.err.println("Database error during Escape Room operation: " + e.getMessage());
                e.printStackTrace(); // Para depuración
            } catch (Exception e) {
                // Captura cualquier otra excepción inesperada
                System.err.println("An unexpected error occurred during Escape Room operation: " + e.getMessage());
                e.printStackTrace(); // Para depuración
            }
        } while (choice != 0);
    }
}
