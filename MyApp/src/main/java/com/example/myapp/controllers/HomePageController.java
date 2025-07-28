package com.example.myapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {
    @FXML private Button todoButton, flashcardButton,timerButton;

    @FXML
    private void goToTodo(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/todo-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/homepage.css");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("TODO List");
        stage.show();
    }

    @FXML
    private void goToFlashcards(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/myapp/fxml/flashcard-main.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Flashcards");
        stage.show();
    }

    @FXML
    private void goToTimer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/example/myapp/fxml/timer.fxml")
        );
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/myapp/css/timer.css").toExternalForm()
        );
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Pomodoro Timer");
        stage.show();
    }
}
