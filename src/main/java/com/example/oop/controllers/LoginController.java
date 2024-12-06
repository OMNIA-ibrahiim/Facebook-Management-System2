package com.example.oop.controllers;

import com.example.oop.models.RegisteredUser;
import com.example.oop.utils.FileManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {

    public void start(Stage stage) {
        // UI Elements
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                // Validate email and password fields
                if (email.isEmpty() || password.isEmpty()) {
                    throw new IllegalArgumentException("Email and Password are required!");
                }

                // Validate email format
                if (!FileManager.validateEmail(email)) {
                    throw new IllegalArgumentException("Invalid email format!");
                }

                // Check if the email exists and the password is correct
                RegisteredUser user = FileManager.getUserByEmailAndPassword(email, password);
                if (user == null) {
                    throw new IllegalArgumentException("Invalid email or password!");
                }

                // Successful login
                new Alert(Alert.AlertType.INFORMATION, "Login Successful!").show();
                // Proceed to next page or dashboard here

            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            } catch (Exception ex) {
                // General exception handling for unexpected errors
                new Alert(Alert.AlertType.ERROR, "An error occurred: " + ex.getMessage()).show();
            }
        });

        // Layout for the login form
        VBox layout = new VBox(10,
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                loginButton);
        layout.setPadding(new Insets(20));

        // Set the scene and stage for login
        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.show();
    }
}