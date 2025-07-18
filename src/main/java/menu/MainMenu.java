package menu;

import dao.RoomDao;
import dao.RoomDaoImplementation;
import model.Room;
import util.DatabaseConnection;
import util.EnvLoader;

import java.sql.SQLException;
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
            // Puedes hacer una prueba de conexión inicial aquí si lo deseas.
            // DatabaseConnection.getInstance().getConnection();

            do {
                System.out.println("\n--- Menú Principal Escape Room ---");
                System.out.println("1. Operaciones CRUD de Rooms");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");
                input = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea

                switch (input) {
                    case 1:
                        handleRoomCrud();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                }
            } while (input != 0);

        } catch (RuntimeException e) {
            System.err.println("Error fatal al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Captura cualquier otra excepción no SQL o Runtime
            System.err.println("Se produjo un error inesperado en el menú principal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    private void handleRoomCrud() {
        int roomChoice;
        do {
            System.out.println("\n--- CRUD de Rooms ---");
            System.out.println("1. Crear Room"); // Cambiado a Crear
            System.out.println("2. Obtener Room por ID");
            System.out.println("3. Listar todas las Rooms");
            System.out.println("4. Actualizar Room");
            System.out.println("5. Eliminar Room");
            System.out.println("0. Volver al menú principal");
            System.out.print("Elige una opción para Rooms: ");
            roomChoice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (roomChoice) {
                    case 1: // Crear Room
                        System.out.print("Tema: "); String theme = scanner.nextLine();
                        System.out.print("Nivel de Dificultad (1-10): "); int difficulty = scanner.nextInt();
                        scanner.nextLine(); // Consumir nueva línea
                        roomDao.createRoom(new Room(theme, difficulty)); // Usar createRoom
                        break;
                    case 2: // Obtener por ID
                        System.out.print("ID de la Room a obtener: "); int idGet = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> optionalRoom = roomDao.getRoomById(idGet); // Recibe un Optional
                        if (optionalRoom.isPresent()) { // Verifica si hay un valor presente
                            System.out.println("Room encontrada: " + optionalRoom.get()); // Obtiene el valor
                        } else {
                            System.out.println("Room con ID " + idGet + " no encontrada.");
                        }
                        break;
                    case 3: // Listar todas
                        List<Room> rooms = roomDao.getAllRooms();
                        if (rooms.isEmpty()) System.out.println("No hay rooms.");
                        else rooms.forEach(System.out::println);
                        break;
                    case 4: // Actualizar
                        System.out.print("ID de la Room a actualizar: "); int idUpdate = scanner.nextInt();
                        scanner.nextLine();
                        Optional<Room> roomToUpdateOptional = roomDao.getRoomById(idUpdate); // Recibe Optional
                        if (roomToUpdateOptional.isPresent()) {
                            Room roomToUpdate = roomToUpdateOptional.get(); // Obtiene la Room
                            System.out.print("Nuevo tema (actual: " + roomToUpdate.getTheme() + "): "); String newTheme = scanner.nextLine();
                            System.out.print("Nuevo nivel de dificultad (actual: " + roomToUpdate.getDifficultyLevel() + "): "); int newDifficulty = scanner.nextInt();
                            scanner.nextLine();
                            roomToUpdate.setTheme(newTheme);
                            roomToUpdate.setDifficultyLevel(newDifficulty);
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
            } catch (SQLException e) { // Captura solo SQLException aquí
                System.err.println("Error de base de datos en la operación de Room: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) { // Captura cualquier otra excepción general
                System.err.println("Error inesperado en la operación de Room: " + e.getMessage());
                e.printStackTrace();
            }
        } while (roomChoice != 0);
    }
}