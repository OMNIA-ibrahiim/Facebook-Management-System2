package com.example.oop.main;

import com.example.oop.controllers.RegistrationController;
import com.example.oop.controllers.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        Button registerButton = new Button("Go to Registration");
        Button loginButton = new Button("Go to Login");

        registerButton.setOnAction(e -> new RegistrationController().start(new Stage()));
        loginButton.setOnAction(e -> new LoginController().start(new Stage()));

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(registerButton, loginButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

