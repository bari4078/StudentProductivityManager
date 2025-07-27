package com.example.myapp.persistence;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskSaver {

    private static final String FILE_NAME = "tasks.txt";  // File to save tasks

    // Save tasks to the file
    public static void save(List<String> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) {
                writer.write(task); // Write each task to the file
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load all tasks from the file
    public static List<String> load() {
        List<String> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Load tasks based on a specific date
    public static List<String> loadTasksByDate(LocalDate date) {
        List<String> tasksForDate = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the date
                if (line.contains(date.toString())) {
                    tasksForDate.add(line);  // Add task to the list for the selected date
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasksForDate;
    }
}
