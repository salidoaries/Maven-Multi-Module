package com.tablemanager.app;

import com.tablemanager.service.TableService;
import com.tablemanager.utility.FileUtility;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TableManagerApp {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            File file = args.length > 0
                    ? TableManagerApp.getFile(args[0].endsWith(".txt") ? args[0] : args[0] + ".txt", scan)
                    : TableManagerApp.promptForFile(scan);

            TableService manager = new TableService(file);

            runWithService(manager, scan);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("\nExiting program. Goodbye!");
            scan.close();
        }
    }

    // --- Extracted for testability ---
    static void runWithService(TableService manager, Scanner scan) throws IOException {
        if (FileUtils.sizeOf(manager.getFile()) > 0) {
            manager.loadTable();
            System.out.println("\nLoaded table from: " + manager.getFile().getName());
            manager.printTable();
        } else {
            int[] dims = askTableDimensions(scan);
            manager.generateNewTable(dims[0], dims[1]);
            System.out.println("\nNew table created successfully:");
            manager.printTable();
        }

        runMenu(manager, scan);
    }

    static File getFile(String fileName, Scanner scan) {
        try {
            if (FileUtility.fileExists(fileName)) {
                return FileUtility.validateFile(fileName);
            }
            return FileUtility.createFile(fileName);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return promptForFile(scan);
        }
    }

    static File promptForFile(Scanner scan) {
        while (true) {
            System.out.print("Enter file name (without extension for new, or full name for existing): ");
            String input = scan.nextLine().trim();
            String fileName = input.endsWith(".txt") ? input : input + ".txt";

            try {
                return FileUtility.fileExists(fileName)
                        ? FileUtility.validateFile(fileName)
                        : FileUtility.createFile(fileName);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static int[] askTableDimensions(Scanner scan) {
        while (true) {
            System.out.print("Enter table dimension (e.g., 3x3): ");
            String input = scan.nextLine().trim();
            String[] parts = input.split("x");

            if (parts.length != 2) {
                System.out.println("Invalid format. Use [rows]x[cols].");
                continue;
            }

            try {
                int rows = Integer.parseInt(parts[0].trim());
                int cols = Integer.parseInt(parts[1].trim());
                if (rows <= 0 || cols <= 0) {
                    System.out.println("Rows and columns must be positive.");
                    continue;
                }
                return new int[]{rows, cols};
            } catch (NumberFormatException e) {
                System.out.println("Invalid numbers. Enter digits only.");
            }
        }
    }

    static void runMenu(TableService manager, Scanner scan) {
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("[search]  - Search");
            System.out.println("[edit]    - Edit");
            System.out.println("[add_row] - Add Row");
            System.out.println("[sort]    - Sort");
            System.out.println("[print]   - Print");
            System.out.println("[reset]   - Reset");
            System.out.println("[x]       - Exit");
            System.out.print("\nChoose an option: ");

            String choice = scan.nextLine().trim().toLowerCase();

            switch (choice) {
                case "search" -> manager.search();
                case "edit" -> manager.edit();
                case "add_row" -> manager.addRow();
                case "sort" -> manager.sortRow();
                case "print" -> manager.printTable();
                case "reset" -> {
                    int[] dims = askTableDimensions(scan);
                    manager.generateNewTable(dims[0], dims[1]);
                    manager.printTable();
                }
                case "x" -> running = false;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
