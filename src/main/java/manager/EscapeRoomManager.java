package manager;

import dao.EscapeRoomDAO;
import dao.EscapeRoomDaoImpl;
import exception.EscapeRoomNotFoundException;
import exception.InvalidInputException;
import model.EscapeRoom;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EscapeRoomManager {
    private final EscapeRoomDAO escapeRoomDao;
    private final InputHelper scannerManager;

    public EscapeRoomManager(InputHelper scannerManager) {
        this.escapeRoomDao = new EscapeRoomDaoImpl(); // Instancia el DAO
        this.scannerManager = scannerManager;          // Inyecta el ScannerManager
    }

    public void handleEscapeRoomCrud() {
        int choice;
        do {
            System.out.println("\n--- CRUD for Escape Rooms ---");
            System.out.println("1. Create Escape Room");
            System.out.println("2. Search Escape Room by ID");
            System.out.println("3. List all Escape Rooms");
            System.out.println("4. Update Escape Room");
            System.out.println("5. Delete Escape Room");
            System.out.println("0. Back to Main Menu");
            choice = scannerManager.readInt("Select a valid option for Escape Rooms Menu: ");

            try {
                switch (choice) {
                    case 1:
                        createEscapeRoom();
                        break;
                    case 2:
                        searchEscapeRoomById();
                        break;
                    case 3:
                        listAllEscapeRooms();
                        break;
                    case 4:
                        updateEscapeRoom();
                        break;
                    case 5:
                        deleteEscapeRoom();
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

    private void createEscapeRoom() throws SQLException {
        String name = scannerManager.readString("Enter Escape Room name: ");
        int totalTickets = scannerManager.readInt("Enter total tickets: ");

        escapeRoomDao.createEscaperoom(new EscapeRoom(name, totalTickets));
        System.out.println("Escape Room '" + name + "' created successfully!");
    }

    private void searchEscapeRoomById() throws SQLException {
        int id = scannerManager.readInt("Enter ID of the Escape Room to search: ");
        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);

        if (optionalEscapeRoom.isPresent()) {
            System.out.println("Escape Room found: " + optionalEscapeRoom.get());
        } else {
            System.out.println("Escape Room with ID " + id + " not found.");
        }
    }

    private void listAllEscapeRooms() throws SQLException {
        List<EscapeRoom> escapeRooms = escapeRoomDao.getAll();
        if (escapeRooms.isEmpty()) {
            System.out.println("No Escape Rooms available.");
        } else {
            System.out.println("--- All Escape Rooms ---");
            escapeRooms.forEach(System.out::println);
        }
    }

    private void updateEscapeRoom() throws SQLException, EscapeRoomNotFoundException {
        int id = scannerManager.readInt("Enter ID of the Escape Room to update: ");
        Optional<EscapeRoom> optionalEscapeRoom = escapeRoomDao.getEscapeRoomById(id);

        if (optionalEscapeRoom.isPresent()) {
            EscapeRoom escapeRoomToUpdate = optionalEscapeRoom.get();
            System.out.println("Current details: " + escapeRoomToUpdate);

            String newName = scannerManager.readString("Enter new name (current: " + escapeRoomToUpdate.getName() + "): ");
            int newTotalTickets = scannerManager.readInt("Enter new total tickets (current: " + escapeRoomToUpdate.getTotalTickets() + "): ");

            escapeRoomToUpdate.setName(newName);
            escapeRoomToUpdate.setTotalTickets(newTotalTickets);

            escapeRoomDao.updateEscaperoom(escapeRoomToUpdate); // Puede lanzar EscapeRoomNotFoundException
            System.out.println("Escape Room with ID " + id + " updated successfully!");
        } else {
            System.out.println("Escape Room with ID " + id + " not found for update.");
        }
    }

    private void deleteEscapeRoom() throws SQLException, EscapeRoomNotFoundException {
        int id = scannerManager.readInt("Enter ID of the Escape Room to delete: ");
        escapeRoomDao.deleteEscaperoom(id); // Puede lanzar EscapeRoomNotFoundException
        System.out.println("Escape Room with ID " + id + " deleted successfully!");
    }
}
