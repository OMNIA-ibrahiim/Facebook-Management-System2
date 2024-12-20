package com.example.oop.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class WelcomeController {

    public void start(Stage stage) {
        //Button
        Button welcomeButton = new Button("Welcome to Facebook");
        welcomeButton.setOnAction(e -> new OptionsController().start(stage));
        welcomeButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        //VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(welcomeButton);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Facebook welcome Page");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

}