package com.example.myapp.controllers;

import com.example.myapp.persistence.TaskSaver;
import com.sun.net.httpserver.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import javafx.scene.control.TextField;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

public class HomePageController {
    @FXML private Button todoButton, flashcardButton,timerButton,Quizbutton;
    @FXML private TextField searchField;
    @FXML
    private void goToTodo(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/searchtodo.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("search todo List");
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

    @FXML
    private void goToQuiz(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/com/example/myapp/fxml/hello-view.fxml"))
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/myapp/css/timer.css").toExternalForm()
        );
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Mental Health Test");
        stage.show();
    }
    @FXML
    private void onWebSearch(ActionEvent event) {
        try {
            String query = URLEncoder.encode(searchField.getText().trim(), "UTF-8");
            URI uri = new URI("https://www.google.com/search?q=" + query);
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openLifehacker() {
        openUri("https://lifehacker.com");
    }

    @FXML
    private void openZenHabits() {
        openUri("https://zenhabits.net");
    }

    @FXML
    private void openGTD() {
        openUri("https://gettingthingsdone.com");
    }


    private void openUri(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}