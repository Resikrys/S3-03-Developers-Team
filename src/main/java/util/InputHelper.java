package util;

import exception.InvalidInputException;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper() {
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

    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid format. Please enter a valid decimal number.");
            }
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (true/false): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.err.println("Please type 'true' or 'false'.");
            }
        }
    }

    public <T extends Enum<T>> T readEnum(Class<T> enumType, String prompt) {
        System.out.println(prompt);
        T[] constants = enumType.getEnumConstants();
        for (int i = 0; i < constants.length; i++) {
            System.out.println((i + 1) + ". " + constants[i]);
        }
        while (true) {
            int choice = readInt("Choose a number (1-" + constants.length + "): ");
            if (choice >= 1 && choice <= constants.length) {
                return constants[choice - 1];
            }
            System.err.println("Invalid choice. Try again.");
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid decimal number.");
            }
        }
    }

    public void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
