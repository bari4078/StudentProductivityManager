package com.example.myapp.controllers;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FlashcardViewController {
    @FXML
    private StackPane flashcardStack;
    @FXML
    private AnchorPane frontPane;
    @FXML
    private AnchorPane backPane;
    @FXML
    private Label frontLabel;
    @FXML
    private Label backLabel;

    private boolean showingFront = true;

    public void setFlashcard(String front, String back) {
        frontLabel.setText(front);
        backLabel.setText(back);

        showingFront = true;


        frontPane.setVisible(true);
        backPane.setVisible(false);

        backPane.setRotate(180);


    }

    @FXML
    public void flipCard() {
        RotateTransition flip = new RotateTransition(Duration.millis(300), flashcardStack);
        flip.setAxis(Rotate.Y_AXIS);

        if (showingFront) {
            flip.setFromAngle(0);
            flip.setToAngle(180);
            backPane.setScaleX(-1);
            backPane.setScaleY(-1);
            backPane.setScaleX(1);
        } else {
            flip.setFromAngle(180);
            flip.setToAngle(0);
        }

        flip.setOnFinished(e -> {
            showingFront = !showingFront;
            frontPane.setVisible(showingFront);
            backPane.setVisible(!showingFront);
        });

        flip.play();
    }
}
