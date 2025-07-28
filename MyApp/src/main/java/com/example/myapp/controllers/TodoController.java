package com.example.myapp.controllers;

import com.example.myapp.persistence.TaskSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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


public class TodoController {

    public Button backButton;
    @FXML
    private TextField taskField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> taskList;

    private ObservableList<String> tasks = FXCollections.observableArrayList();
    private ContextMenu contextMenu;

    @FXML
    public void initialize() {
        // Load tasks from file when the page is initialized
        List<String> loaded = TaskSaver.load();
        tasks = FXCollections.observableArrayList(loaded);
        taskList.setItems(tasks);
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        // Create context menu for task options
        contextMenu = new ContextMenu();
        MenuItem completedItem = new MenuItem("Mark as Completed");
        MenuItem deleteItem = new MenuItem("Delete Task");
        contextMenu.getItems().addAll(completedItem, deleteItem);

        // Handle click on a task
        taskList.setOnMouseClicked(event -> handleTaskSelection(event));

        // Set actions for "Completed" and "Delete" options
        completedItem.setOnAction(e -> markAsCompleted());
        deleteItem.setOnAction(e -> deleteTask());

        // Initialize the hour and minute spinners
        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 12);
        hourSpinner.setValueFactory(hourValueFactory);
        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteValueFactory);
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
            saveTasks();  // Save tasks after marking as completed
        }
    }

    private void deleteTask() {
        String selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            taskList.setStyle("-fx-background-color: transparent;");
            saveTasks();  // Save tasks after deletion
        }
    }

    // Add task with its deadline (date and time)
    @FXML
    public void addTask() {
        String taskText = taskField.getText();
        LocalDate date = datePicker.getValue();
        int h = hourSpinner.getValue(), m = minuteSpinner.getValue();
        if (!taskText.isEmpty() && date != null) {
            String taskWithDeadline = taskText + " - Due: " + date + " " + String.format("%02d:%02d", h, m);
            tasks.add(taskWithDeadline);
            taskList.setItems(tasks); // update UI
            TaskSaver.save(tasks);     // persist to file:contentReference[oaicite:10]{index=10}

            // Clear input fields
            taskField.clear();
            datePicker.setValue(null);
            hourSpinner.getValueFactory().setValue(12);
            minuteSpinner.getValueFactory().setValue(0);
        }
    }

    // Go back to the Login page
    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        // Save tasks then return to homepage
        TaskSaver.save(tasks);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/homepage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        //ei line na korle, css style ashbe na
        scene.getStylesheets().add(getClass().getResource("/com/example/myapp/css/homepage.css").toExternalForm());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }


    // Save tasks to a text file
    private void saveTasks() {
        TaskSaver.save(tasks);  // Save the task list to the file
    }

}
