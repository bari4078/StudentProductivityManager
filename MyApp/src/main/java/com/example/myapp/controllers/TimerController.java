package com.example.myapp.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TimerController implements Initializable {


    @FXML private BorderPane rootPane;
    @FXML private ToggleButton pomodoroBtn;
    @FXML private ToggleButton shortBreakBtn;
    @FXML private ToggleButton longBreakBtn;
    @FXML private Label timerLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private Button backButton;

    private ToggleGroup modeGroup;
    private Timeline timeline;
    private int totalSeconds;
    private boolean running = false;

    private static final int POMO_SECONDS       = 25 * 60;
    private static final int SHORT_BREAK_SECONDS = 5 * 60;
    private static final int LONG_BREAK_SECONDS  = 15 * 60;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        modeGroup = new ToggleGroup();
        pomodoroBtn.setToggleGroup(modeGroup);
        shortBreakBtn.setToggleGroup(modeGroup);
        longBreakBtn.setToggleGroup(modeGroup);


        pomodoroBtn.setSelected(true);
        setModeDuration(POMO_SECONDS);

        pomodoroBtn.setOnAction(e -> onModeSelected(POMO_SECONDS));
        shortBreakBtn.setOnAction(e -> onModeSelected(SHORT_BREAK_SECONDS));
        longBreakBtn.setOnAction(e -> onModeSelected(LONG_BREAK_SECONDS));
        startButton.setOnAction(this::handleStartPause);
        resetButton.setOnAction(e -> resetTimer());
        backButton.setOnAction(e -> {
            try {
                goToHome(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        updateLabel();
    }

    private void onModeSelected(int seconds) {
        stopTimeline();
        setModeDuration(seconds);
        updateLabel();
        startButton.setText("Start");
        running = false;
    }


    private void setModeDuration(int seconds) {
        this.totalSeconds = seconds;
    }

    private void handleStartPause(ActionEvent event) {
        if (!running) {

            if (timeline == null) {

                timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        ae -> tick()
                ));
                timeline.setCycleCount(Timeline.INDEFINITE);
            }
            timeline.play();
            startButton.setText("Pause");
            running = true;

        } else {

            if (timeline != null) {
                timeline.pause();
            }
            startButton.setText("Start");
            running = false;
        }
    }


    private void tick() {
        totalSeconds--;
        updateLabel();

        if (totalSeconds <= 0) {

            stopTimeline();
            onTimerFinished();
        }
    }

    private void stopTimeline() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }


    private void resetTimer() {
        stopTimeline();
        ToggleButton selected = (ToggleButton) modeGroup.getSelectedToggle();
        if (selected == pomodoroBtn)       setModeDuration(POMO_SECONDS);
        else if (selected == shortBreakBtn) setModeDuration(SHORT_BREAK_SECONDS);
        else if (selected == longBreakBtn)  setModeDuration(LONG_BREAK_SECONDS);

        updateLabel();
        startButton.setText("Start");
        running = false;
    }


    private void updateLabel() {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void onTimerFinished() {

        startButton.setText("Start");
        running = false;
        new Alert(Alert.AlertType.INFORMATION, "Time’s up!").show();
    }

    @FXML
    private void goToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/myapp/fxml/homepage.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/example/myapp/css/homepage.css").toExternalForm()
        );
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Home");
        stage.show();
    }
}

