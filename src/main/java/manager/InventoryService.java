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
    // Calcula el valor total del inventario
    public double calculateInventoryValue() throws SQLException {
        double total = 0;

        for (ClueObject clue : clueDao.getAllClues()) {
            total += clue.getPrice().doubleValue();
        }

        for (DecorationObject deco : decorationDao.getAllDecorations()) {
            total += deco.getPrice().doubleValue();
        }

        return total;
    }
    // Menú simplificado: solo muestra resumen
    public void showMenu(InputHelper scanner) {
        try {
            showInventorySummary();
        } catch (SQLException e) {
            System.out.println("❌ Error loading inventory: " + e.getMessage());
        }
    }
}

