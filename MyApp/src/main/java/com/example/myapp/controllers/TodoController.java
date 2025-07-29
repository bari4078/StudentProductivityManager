package com.example.myapp.controllers;

import com.example.myapp.persistence.TaskSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TodoController  {

    @FXML
    public Button backButton;
    public Button searchButton;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minutespinner;

    @FXML
    private TextField taskField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> taskList;

    private ObservableList<String> tasks = FXCollections.observableArrayList();
    private ContextMenu contextMenu;

    @FXML
    public void initialize() {
        // Load tasks from the TaskSaver class
        List<String> loaded = TaskSaver.load();
        tasks = FXCollections.observableArrayList(loaded);
        taskList.setItems(tasks);

        // Initialize the spinner value factories for hour and minute (24-hour format)
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12)); // Corrected to 0-23 for 24-hour format
        minutespinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // Context menu for task operations
        contextMenu = new ContextMenu();
        MenuItem completedItem = new MenuItem("Mark as Completed");
        MenuItem deleteItem = new MenuItem("Delete Task");
        contextMenu.getItems().addAll(completedItem, deleteItem);

        taskList.setOnMouseClicked(event -> handleTaskSelection(event));

        completedItem.setOnAction(e -> markAsCompleted());
        deleteItem.setOnAction(e -> deleteTask());
    }

    private void handleTaskSelection(MouseEvent event) {
        if (event.getClickCount() == 1) {
            String selectedTask = taskList.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskList.setStyle("-fx-background-color: lightblue;");
                contextMenu.show(taskList, event.getScreenX(), event.getScreenY());
            }
        }
    }

    private void markAsCompleted() {
        String selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            tasks.add(selectedTask + " - Completed");
            taskList.setStyle("-fx-background-color: transparent;");
            saveTasks();
        }
    }

    private void deleteTask() {
        String selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            taskList.setStyle("-fx-background-color: transparent;");
            saveTasks();
        }
    }

    @FXML
    public void addTask() {
        String taskText = taskField.getText();
        LocalDate date = datePicker.getValue();
        int h = hourSpinner.getValue(), m = minutespinner.getValue();
        if (!taskText.isEmpty() && date != null) {
            String taskWithDeadline = taskText + " - Due: " + date + " " + String.format("%02d:%02d", h, m);
            tasks.add(taskWithDeadline);
            taskList.setItems(tasks);
            TaskSaver.save(tasks);

            taskField.clear();
            datePicker.setValue(null);
            hourSpinner.getValueFactory().setValue(12);  // Reset hour spinner to default value
            minutespinner.getValueFactory().setValue(0); // Reset minute spinner to default value
        }
    }

    // Go back to the HomePage
    @FXML
    public void goTosearchtodo(ActionEvent event) throws IOException {
        TaskSaver.save(tasks);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/searchtodo.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/myapp/css/homepage.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }

    private void saveTasks() {
        TaskSaver.save(tasks);
    }


}
