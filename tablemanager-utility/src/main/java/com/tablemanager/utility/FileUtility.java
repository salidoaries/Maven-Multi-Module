package com.tablemanager.utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FileExistsException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtility {

    public static File createFile(String fileName) throws IOException {
        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
        }

        File file = new File(fileName);

        if (file.exists()) {
            throw new FileExistsException("File already exists: " + fileName);
        }

        // Create parent directories if they don't exist
        FileUtils.forceMkdirParent(file);

        // Create the file itself
        FileUtils.touch(file);

        return file;
    }

    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile();
    }

    public static File validateFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!fileExists(fileName)) {
            throw new IOException("File not found: " + fileName);
        }
        return file;
    }

    public static List<String> readFile(String fileName) throws IOException {
        File file = validateFile(fileName);
        return FileUtils.readLines(file, StandardCharsets.UTF_8);
    }

    public static void writeFile(File file, List<String> lines) throws IOException {
        // Ensure parent directories exist
        FileUtils.forceMkdirParent(file);

        // Write lines with explicit UTF-8 encoding
        FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), lines);
    }
}
