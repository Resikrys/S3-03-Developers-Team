package menu;

import dao.RoomDao;
import dao.RoomDaoImplementation;
import model.Room;
import util.DatabaseConnection;
import util.EnvLoader;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private RoomDao roomDao;
    private Scanner scanner;

    public MainMenu() {
        EnvLoader.getInstance(); // Solo llamamos a getInstance para asegurar que se cargue

        this.roomDao = new RoomDaoImplementation();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int input;
        try {
            do {
                System.out.println("\n--- Main Menu Escape Room ---");
                System.out.println("1. CRUD Operations for Rooms");
                System.out.println("2. Another CRUD Operations");
                System.out.println("0. Exit");
                System.out.print("Select a valid menu option: ");
                input = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (input) {
                    case 1:
                        handleRoomCrud();
                        break;
                    case 2:
                        //AnotherOption();
                        break;
                    case 0:
                        System.out.println(" Bye bye! ");
                        break;
                    default:
                        System.out.println("Invalid option, try again.");
                }
            } while (input != 0);

        } catch (RuntimeException e) { // Captura excepciones lanzadas por EnvLoader o DatabaseConnection
            System.err.println("Error fatal al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Se produjo un error inesperado en el menú principal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    // Métodos para manejar los submenús (copiados de MainApp anterior)
    private void handleRoomCrud() {
        int roomInput;
        do {
            System.out.println("\n--- CRUD de Rooms ---");
            System.out.println("1. Create Room");
            System.out.println("2. Search Room by ID");
            System.out.println("3. List all Rooms");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to principal menu");
            System.out.print("Select a valid Rooms menu option: ");
            roomInput = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            try {
                switch (roomInput) {
                    case 1: // Guardar Room
                        System.out.print("Tema: "); String theme = scanner.nextLine();
                        System.out.print("Room difficulty level: "); int difficulty_level = scanner.nextInt();
                        scanner.nextLine(); // Consumir nueva línea
                        roomDao.createRoom(new Room(theme, difficulty_level));
                        break;
                    case 2: // Obtener por ID
                        System.out.print("ID de la Room a obtener: "); int idGet = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> room = roomDao.getRoomById(idGet);
                        if (room != null) System.out.println("Room encontrada: " + room);
                        else System.out.println("Room con ID " + idGet + " no encontrada.");
                        break;
                    case 3: // Listar todas
                        List<Room> rooms = roomDao.getAllRooms();
                        if (rooms.isEmpty()) System.out.println("No hay rooms.");
                        else rooms.forEach(System.out::println);
                        break;
                    case 4: // Actualizar
                        System.out.print("ID de la Room a actualizar: "); int idUpdate = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> roomToUpdate = roomDao.getRoomById(idUpdate);
                        if (roomToUpdate != null) {
                            System.out.print("Nuevo tema (actual: " + roomToUpdate.getTheme() + "): "); String newTheme = scanner.nextLine();
                            System.out.print("Nueva capacidad (actual: " + roomToUpdate.getDifficulty_level() + "): "); int newCapacity = scanner.nextInt();
                            scanner.nextLine();
                            roomToUpdate.setTheme(newTheme);
                            roomToUpdate.setDifficulty_level(newCapacity);
                            roomDao.updateRoom(roomToUpdate);
                        } else {
                            System.out.println("Room con ID " + idUpdate + " no encontrada para actualizar.");
                        }
                        break;
                    case 5: // Eliminar
                        System.out.print("ID de la Room a eliminar: "); int idDelete = scanner.nextInt();
                        scanner.nextLine();
                        roomDao.deleteRoom(idDelete);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.err.println("Error en la operación de Room: " + e.getMessage());
                e.printStackTrace();
            }
        } while (roomInput != 0);
    }
}
