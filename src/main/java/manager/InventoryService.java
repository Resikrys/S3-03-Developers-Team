package manager;

import model.ClueObject;
import model.DecorationObject;
import model.Room;

import java.util.List;

public class InventoryService {
    private RoomManager roomManager;
    private ClueManager clueManager;
    private DecorationManager decorationManager;

    public InventoryService(RoomManager roomManager, DecorationManager decorationManager, ClueManager clueManager) {
        this.roomManager = roomManager;
        this.decorationManager = decorationManager;
        this.clueManager = clueManager;
    }

    // Muestra el resumen del inventario actual con cantidad de cada elemento y valor total
    public void showInventorySummary() {
        List<Room> rooms = roomManager.getAllRooms();
        List<ClueObject> clues = clueManager.getAllClues();
        List<DecorationObject> decorations = decorationManager.getAllDecorations();

        System.out.println("------ CURRENT INVENTORY ------");
        System.out.println("Available rooms: " + rooms.size());
        System.out.println("Available clues: " + clues.size());
        System.out.println("Available decorations: " + decorations.size());

        double totalValue = calculateInventoryValue();
        System.out.printf("Total inventory value: %.2f €\n", totalValue);
    }

    // Calcula el valor total del inventario (solo pistas y decoración)
    public double calculateInventoryValue() {
        double total = 0;

        for (ClueObject clue : clueManager.getAllClues()) {
            total += clue.getPrice();
        }

        for (DecorationObject deco : decorationManager.getAllDecorations()) {
            total += deco.getPrice();
        }

        return total;
    }

    // Elimina una sala por su ID
    public void removeRoom(int id) {
        roomManager.deleteRoomById(id);  // Este método debe existir en RoomManager
        System.out.println("Room removed.");
    }

    // Elimina una pista por su ID
    public void removeClue(int id) {
        clueManager.deleteClueById(id);  // Este método debe existir en ClueManager
        System.out.println("Clue removed.");
    }

    // Elimina un objeto de decoración por su ID
    public void removeDecoration(int id) {
        decorationManager.deleteDecorationById(id);  // Este método debe existir en DecorationManager
        System.out.println("Decoration removed.");
    }
}
