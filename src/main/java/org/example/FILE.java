package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FILE {
    public static List<String> readFromFile(String fileName) {
        try {
            return Files.lines(Paths.get(fileName)).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void writeToFile(String fileName, List<String> lines) {
        try {
            Files.write(Paths.get(fileName), lines);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
