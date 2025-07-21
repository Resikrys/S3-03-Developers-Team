package manager;

import dao.ClueDao;
import dao.DecorationObjectDAO;
import dao.RoomDao;
import model.ClueObject;
import model.DecorationObject;
import model.Room;
import util.InputHelper;

import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final RoomDao roomDao;
    private final ClueDao clueDao;
    private final DecorationObjectDAO decorationDao;

    public InventoryService(RoomDao roomDao, ClueDao clueDao, DecorationObjectDAO decorationDao) {
        this.roomDao = roomDao;
        this.clueDao = clueDao;
        this.decorationDao = decorationDao;
    }

    // Mostrar resumen del inventario
    public void showInventorySummary() throws SQLException {
        List<Room> rooms = roomDao.getAllRooms();
        List<ClueObject> clues = clueDao.getAllClues();
        List<DecorationObject> decorations = decorationDao.getAllDecorations();

        System.out.println("------ CURRENT INVENTORY ------");
        System.out.println("Available rooms: " + rooms.size());
        System.out.println("Available clues: " + clues.size());
        System.out.println("Available decorations: " + decorations.size());

        double totalValue = calculateInventoryValue();
        System.out.printf("Total inventory value: %.2f €\n", totalValue);
    }

    // Calcula el valor total del inventario (suma de precios de pistas y decoración)
    public double calculateInventoryValue() throws SQLException{
        double total = 0;

        for (ClueObject clue : clueDao.getAllClues()) {
            total += clue.getPrice().doubleValue();
        }

        for (DecorationObject deco : decorationDao.getAllDecorations()) {
            total += deco.getPrice().doubleValue();
        }

        return total;
    }

    // Elimina una sala por su ID
    public void removeRoom(int id)throws SQLException {
        roomDao.deleteRoom(id);
        System.out.println("Room removed.");
    }

    // Elimina una pista por su ID
    public void removeClue(int id)throws SQLException {
        clueDao.deleteClue(id);
        System.out.println("Clue removed.");
    }

    // Elimina un objeto de decoración por su ID
    public void removeDecoration(int id)throws SQLException {
        decorationDao.deleteDecoration(id);
        System.out.println("Decoration removed.");
    }
    // Menú interactivo integrado
    public void showInteractiveInventoryMenu(InputHelper scanner) {
        int option;
        do {
            System.out.println("\n======= INVENTORY MENU =======");
            System.out.println("1. Show inventory summary");
            System.out.println("2. Remove a room by ID");
            System.out.println("3. Remove a clue by ID");
            System.out.println("4. Remove a decoration by ID");
            System.out.println("0. Back to main menu");
            System.out.println("==============================");

            option = scanner.readInt("Choose an option: ");

            try {
                switch (option) {
                    case 1 -> showInventorySummary();
                    case 2 -> {
                        int id = scanner.readInt("Enter room ID to remove: ");
                        removeRoom(id);
                        showInventorySummary();
                    }
                    case 3 -> {
                        int id = scanner.readInt("Enter clue ID to remove: ");
                        removeClue(id);
                        showInventorySummary();
                    }
                    case 4 -> {
                        int id = scanner.readInt("Enter decoration ID to remove: ");
                        removeDecoration(id);
                        showInventorySummary();
                    }
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }

        } while (option != 0);
    }
}
