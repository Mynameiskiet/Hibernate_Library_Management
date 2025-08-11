package com.tuankiet.cli.helpers;

import com.tuankiet.enums.BookCategory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

/**
 * Helper class for reading various types of input from the console.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class InputHelper {

    private final Scanner scanner;

    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Read a line of input from the console.
     * 
     * @return the input line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Read a string with a prompt.
     * 
     * @param prompt the prompt to display
     * @return the input string, trimmed
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return readLine().trim();
    }

    /**
     * Read a string with validation for empty input.
     * 
     * @param prompt the prompt to display
     * @param allowEmpty whether to allow empty input
     * @return the input string
     */
    public String readString(String prompt, boolean allowEmpty) {
        String input;
        do {
            input = readString(prompt);
            if (!allowEmpty && input.isEmpty()) {
                System.out.println("❌ Input cannot be empty. Please try again.");
            }
        } while (!allowEmpty && input.isEmpty());
        return input;
    }

    /**
     * Read an integer from input with validation.
     * 
     * @return the integer value
     */
    public int readInt() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.print("❌ Invalid input. Please enter a number: ");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    /**
     * Read an integer with a prompt.
     * 
     * @param prompt the prompt to display
     * @return the integer value
     */
    public int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    /**
     * Read a UUID from input with validation.
     * 
     * @param prompt the prompt to display
     * @return the UUID or null if empty input
     */
    public UUID readUuid(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (input.isEmpty()) {
                return null; // Allow empty input for optional UUIDs
            }
            try {
                return UUID.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid UUID format. Please try again.");
            }
        }
    }

    /**
     * Read a date from input with validation.
     * 
     * @param prompt the prompt to display
     * @return the LocalDate or null if empty input
     */
    public LocalDate readDate(String prompt) {
        while (true) {
            String dateString = readString(prompt + " (YYYY-MM-DD): ");
            if (dateString.isEmpty()) {
                return null; // Allow empty input for optional dates
            }
            try {
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    /**
     * Read a book category from input with validation.
     * 
     * @param prompt the prompt to display
     * @return the selected BookCategory
     */
    public BookCategory readBookCategory(String prompt) {
        System.out.println(prompt);
        System.out.println("Available categories: " + Arrays.toString(BookCategory.values()));
        while (true) {
            String input = readString("Enter category: ").toUpperCase();
            try {
                return BookCategory.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid category. Please choose from the list.");
            }
        }
    }

    /**
     * Read a boolean value from input.
     * 
     * @param prompt the prompt to display
     * @return true for yes, false for no
     */
    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readString(prompt + " (yes/no): ").toLowerCase();
            if ("yes".equals(input) || "y".equals(input)) {
                return true;
            } else if ("no".equals(input) || "n".equals(input)) {
                return false;
            } else {
                System.out.println("❌ Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    /**
     * Close the scanner when done.
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
