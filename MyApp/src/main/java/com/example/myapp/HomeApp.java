package com.example.myapp;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HomeApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HomeApp.class.getResource("/com/example/myapp/fxml/animation.fxml"));
        Pane animationRoot = fxmlLoader.load();


        Scene animationScene = new Scene(animationRoot, 800, 600);
        stage.setScene(animationScene);
        stage.show();

        applyFadeIn(animationRoot, stage);
    }

    private void applyFadeIn(Pane animationRoot, Stage stage) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), animationRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);


        fadeIn.setOnFinished(event -> applyFadeOut(animationRoot, stage));

        fadeIn.play();
    }

    private void applyFadeOut(Pane animationRoot, Stage stage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), animationRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);


        fadeOut.setOnFinished(event -> {
            try {
                loadHomePage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }

    private void loadHomePage(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HomeApp.class.getResource("fxml/homepage.fxml"));
        Pane homepageRoot = fxmlLoader.load();

        Scene homepageScene = new Scene(homepageRoot, 800, 600);
        homepageScene.getStylesheets().add(
                HomeApp.class.getResource("/com/example/myapp/css/homepage.css").toExternalForm()
        );


        stage.setScene(homepageScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
