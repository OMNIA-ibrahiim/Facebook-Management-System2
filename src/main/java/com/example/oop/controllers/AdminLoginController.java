package com.example.oop.controllers;

import com.example.oop.models.Admin;
import com.example.oop.utils.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminLoginController {
    public void start(Stage stage) {

        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button BackButton =new Button("Back");

        BackButton.setOnAction(e -> new OptionsController().start(stage));

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            try {
                if (email.isEmpty() || password.isEmpty())
                    throw new IllegalArgumentException("Email and Password are required!");

                else if (!AdminManager.validateEmail(email))
                    throw new IllegalArgumentException("Invalid email format!");

                else {
                    Admin admin = AdminManager.getAdminByEmailAndPassword(email, password);
                    if (admin == null)
                        throw new IllegalArgumentException("Invalid email or password!");
                    else
                        new AdminProfileController().start(stage,admin);
                }
            } catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "An error occurred: " + ex.getMessage()).show();
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-text-fill: white;");
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-text-fill: white;");
        layout.getChildren().addAll(
                emailLabel, emailField,
                passwordLabel,passwordField,
                loginButton,
                BackButton
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #18191A;");

        // Scene
        Scene scene = new Scene(layout,300,400);
        stage.setScene(scene);
        stage.setTitle("Login Form");

        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }
}