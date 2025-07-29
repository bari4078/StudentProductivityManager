package com.example.myapp.controllers;

import com.example.myapp.persistence.TaskSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class addtaskcontroller {

    @FXML
    private DatePicker datePicker;  // Date Picker for selecting a date

    @FXML
    private Button searchButton;  // Search button to trigger task search

    @FXML
    private ListView<String> taskList;  // ListView to display tasks for the selected date

    private final ObservableList<String> tasks = FXCollections.observableArrayList();

    // Search for tasks on a specific date
    @FXML
    public void searchTasksByDate() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate != null) {
            // Fetch tasks for the selected date
            tasks.clear();  // Clear the current tasks in the list
            tasks.addAll(TaskSaver.loadTasksByDate(selectedDate));  // Fetch tasks and add them to the list

            // Check if the task list is empty
            if (tasks.isEmpty()) {
                tasks.add("No tasks for this date");  // Add a message if no tasks found
            }

            taskList.setItems(tasks);  // Set tasks to ListView
        }
    }


    @FXML
    public void goToTodoList() throws IOException {
        // Load the Todo page
        FXMLLoader todoLoader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/todo-view.fxml"));
        Scene todoScene = new Scene(todoLoader.load(), 800, 600);
        Stage stage = (Stage) searchButton.getScene().getWindow();
        stage.setScene(todoScene);
        stage.show();
    }

    @FXML
    public void gohome(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/homepage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/myapp/css/homepage.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();

    }

}