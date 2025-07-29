package com.example.myapp.controllers;

import com.example.myapp.models.TimerState;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TimerController implements Initializable {


    @FXML private BorderPane rootPane;
    @FXML private ToggleButton pomodoroBtn;
    @FXML private ToggleButton shortBreakBtn;
    @FXML private ToggleButton longBreakBtn;
    @FXML private Label timerLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private Button backButton;
    @FXML private ProgressBar progressBar;
    private int initialSeconds;
    private ToggleGroup modeGroup;
    private Timeline timeline;
    private int totalSeconds;
    private boolean running = false;

    private static final int POMO_SECONDS       = 25 * 60;
    private static final int SHORT_BREAK_SECONDS = 5 * 60;
    private static final int LONG_BREAK_SECONDS  = 15 * 60;
    private static final Path STATE_FILE = Paths.get("timerState.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private String currentMode = "pomodoro";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Files.exists(STATE_FILE)) {
            try {
                TimerState st = mapper.readValue(STATE_FILE.toFile(), TimerState.class);
                this.initialSeconds = st.initialSeconds;
                this.totalSeconds   = st.totalSeconds;
                this.running        = st.running;
                // re‑select toggle
                switch (st.mode) {
                    case "short" -> shortBreakBtn.setSelected(true);
                    case "long"  -> longBreakBtn.setSelected(true);
                    default      -> pomodoroBtn.setSelected(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // fallback to default
                pomodoroBtn.setSelected(true);
                setModeDuration(POMO_SECONDS);
            }
        } else {
            // first run: set defaults
            pomodoroBtn.setSelected(true);
            setModeDuration(POMO_SECONDS);
        }

        modeGroup = new ToggleGroup();
        pomodoroBtn.setToggleGroup(modeGroup);
        shortBreakBtn.setToggleGroup(modeGroup);
        longBreakBtn.setToggleGroup(modeGroup);

        modeGroup = new ToggleGroup();
        pomodoroBtn.setToggleGroup(modeGroup);
        shortBreakBtn.setToggleGroup(modeGroup);
        longBreakBtn.setToggleGroup(modeGroup);

        pomodoroBtn.setOnAction(e -> onModeSelected(POMO_SECONDS, "pomodoro"));
        shortBreakBtn.setOnAction(e -> onModeSelected(SHORT_BREAK_SECONDS, "short"));
        longBreakBtn.setOnAction(e -> onModeSelected(LONG_BREAK_SECONDS, "long"));

        startButton.setOnAction(this::handleStartPause);
        resetButton.setOnAction(e -> {
            resetTimer();
            saveState();                // ← NEW: persist after reset
        });
        backButton.setOnAction(e -> {
            try { goToHome(e); }
            catch (IOException ex) { throw new RuntimeException(ex); }
        });

        updateLabel();

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1), timerLabel);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);

        FadeTransition glow = new FadeTransition(Duration.seconds(1.5), timerLabel);
        glow.setFromValue(1.0);
        glow.setToValue(0.85);
        glow.setCycleCount(Animation.INDEFINITE);
        glow.setAutoReverse(true);

        pulse.play();
        glow.play();


        progressBar.sceneProperty().addListener((obs, o, scene) -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Node bar = progressBar.lookup(".bar");
                    if (bar != null) startShimmerOn(bar);
                });
            }
        });
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(evt -> saveState());
            }
        });
    }
    private void startShimmerOn(Node bar) {
        Timeline shimmer = new Timeline(
                new KeyFrame(Duration.ZERO,   e -> applyGradient(bar, 0)),
                new KeyFrame(Duration.seconds(3), e -> applyGradient(bar, 1.0))
        );
        shimmer.setCycleCount(Animation.INDEFINITE);
        shimmer.setAutoReverse(true);
        shimmer.play();
    }

    private void applyGradient(Node bar, double t) {
        String css = String.format(
                "-fx-background-color: linear-gradient(to right," +
                        "#cfc9bf 0%%," +
                        "#f4f0e0 %.1f%%," +
                        "#f4f0e0 %.1f%%," +
                        "#cfc9bf 100%%);",
                t * 100, Math.min(t + 0.1, 1.0) * 100
        );
        bar.setStyle(css);
    }
    private void onModeSelected(int seconds, String modeKey) {
        currentMode = modeKey;
        stopTimeline();
        setModeDuration(seconds);
        updateLabel();
        startButton.setText("Start");
        running = false;
        saveState();
    }


    private void setModeDuration(int seconds) {
        this.totalSeconds = seconds;
        this.initialSeconds = seconds;
        if (progressBar != null) {
            progressBar.setProgress(0);
        }
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
        double progress = (double)(initialSeconds - totalSeconds) / initialSeconds;
        progressBar.setProgress(progress);
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


    private void setShimmer(double t) {

        String cssGradient = String.format(
                "-fx-accent: linear-gradient(to right, " +
                        "#cfc9bf 0%%, " +
                        "#f4f0e0 %.1f%%, " +
                        "#f4f0e0 %.1f%%, " +
                        "#cfc9bf 100%%);",
                t * 100,
                Math.min(t + 0.1, 1.0) * 100
        );
        progressBar.setStyle(cssGradient);
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

    private void saveState() {
        try {
            TimerState st = new TimerState();
            st.initialSeconds = this.initialSeconds;
            st.totalSeconds   = this.totalSeconds;
            st.mode           = this.currentMode;
            st.running        = this.running;
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(STATE_FILE.toFile(), st);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}

