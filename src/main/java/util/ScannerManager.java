package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerManager {
    private final Scanner scanner;

    public ScannerManager() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    public Integer readOptionalInt(String prompt) {
        String input = readString(prompt);
        if (input.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format. Value will be set to NULL or ignored.");
            return null; // O podrías relanzar una excepción personalizada si quieres que sea un error
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
