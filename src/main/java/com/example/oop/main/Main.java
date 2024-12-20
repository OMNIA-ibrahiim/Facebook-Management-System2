package com.example.oop.main;

import com.example.oop.controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new WelcomeController().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}