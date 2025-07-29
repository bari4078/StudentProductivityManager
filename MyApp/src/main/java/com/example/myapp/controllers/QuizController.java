package com.example.myapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizController {
    private static int cnt=1;
    private static int finalscore=0;
    public ToggleGroup group2;
    public ToggleGroup group3;
    public ToggleGroup group4;
    public ToggleGroup group5;
    @FXML
    private ToggleGroup group1;

    @FXML
    private RadioButton option1;

    @FXML
    private RadioButton option2;

    @FXML
    private RadioButton option3;


    @FXML
    public void startQuiz(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q1.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }


    public void gobacktohomepage(ActionEvent actionEvent) throws IOException {

        cnt = 1;
        finalscore = 0;


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/hello-view.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();


        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();


    }



    public void gotoNextQuiz(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q1.fxml"));
        // Load the first question page (q1.fxml)
        if(cnt==1) {
            if(group1.getSelectedToggle().equals(option1)){
            }
            else if(group1.getSelectedToggle().equals(option2)){
                finalscore+=5;
            }
            else{
                finalscore+=10;
            }
            loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q2.fxml"));
        }

        else if(cnt==2){
            loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q3.fxml"));
            if(group2.getSelectedToggle().equals(option1)){

            }
            else if(group2.getSelectedToggle().equals(option2)){
                finalscore+=5;
            }
            else{
                finalscore+=10;
            }
        }

        else if(cnt==3){
            if(group3.getSelectedToggle().equals(option1)){

            }
            else if(group3.getSelectedToggle().equals(option2)){
                finalscore+=5;
            }
            else{
                finalscore+=10;
            }
            loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q4.fxml"));
        }

        else if(cnt==4){
            if(group4.getSelectedToggle().equals(option1)){

            }
            else if(group4.getSelectedToggle().equals(option2)){
                finalscore+=5;
            }
            else{
                finalscore+=10;
            }
            loader = new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/q5.fxml"));
//            System.out.println(finalscore);
        }

        else if(cnt==5){

            if(group5.getSelectedToggle().equals(option1)){

            }
            else if(group5.getSelectedToggle().equals(option2)){
                finalscore+=5;
            }
            else{
                finalscore+=10;
            }

            if(finalscore>=40){
                loader= new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/highscore.fxml"));
            }
            else if(finalscore>=25){
                loader= new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/highscore2.fxml"));
            }
            else{
                loader= new FXMLLoader(getClass().getResource("/com/example/myapp/fxml/highscore3.fxml"));
            }

        }

        cnt++;
        Parent root = loader.load();


        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();


        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToHomePage(ActionEvent event) throws IOException {

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
