package util;

import exception.InvalidInputException;

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
            throw new InvalidInputException("Invalid number format for optional integer: '" + input + "'", e);
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
