module com.example.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;


    opens com.example.myapp to javafx.fxml;
    exports com.example.myapp;
    exports com.example.myapp.controllers;
    opens com.example.myapp.controllers to javafx.fxml;

    opens com.example.myapp.models to com.fasterxml.jackson.databind;
}