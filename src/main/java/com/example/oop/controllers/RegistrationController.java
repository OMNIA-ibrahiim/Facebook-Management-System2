package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.FileManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationController {
    // Restrict phone field to only allow numeric input

    public void start(Stage stage) {
        // UI Elements
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();  // Confirm Password Field
        TextField birthdateField = new TextField();
        ComboBox<String> genderComboBox = new ComboBox<>();
        TextField phoneField = new TextField();
        TextField idField = new TextField();

        genderComboBox.getItems().addAll("Male", "Female", "Other");
        Button registerButton = new Button("Register");

        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();  // Get Confirm Password
            String birthdate = birthdateField.getText();
            String gender = genderComboBox.getValue();
            String phone = phoneField.getText();
            String id = idField.getText();

            try {
                // Validate fields
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthdate.isEmpty() || gender == null || phone.isEmpty() || id.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required!");
                }

                // Validate email format
                if (!FileManager.validateEmail(email)) {
                    throw new IllegalArgumentException("Invalid email format!");
                }

                // Validate password length
                if (!FileManager.validatePassword(password)) {
                    throw new IllegalArgumentException("Password must be at least 8 characters!");
                }

                // Check if password and confirm password match
                if (!password.equals(confirmPassword)) {
                    throw new IllegalArgumentException("Passwords do not match!");
                }
                // Check if the email is already in use
                if (FileManager.isDuplicateEmail(email)) {
                    throw new IllegalArgumentException("Email already exists!");
                }

                // Create a new user and save it to the file
                RegularUser  user = new RegularUser(name, email, password, birthdate, gender, phone, id);
                FileManager.saveUser(user);

                // Show success message
                new Alert(Alert.AlertType.INFORMATION, "Registration Successful!").show();
            } catch (IllegalArgumentException ex) {
                // Show error message
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            } catch (Exception ex) {
                // General exception handling (for other unexpected errors)
                new Alert(Alert.AlertType.ERROR, "An error occurred: " + ex.getMessage()).show();
            }
        });

        // Layout for the registration form
        VBox layout = new VBox(10,
                new Label("Name"), nameField,
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                new Label("Confirm Password"), confirmPasswordField,  // Add Confirm Password Label
                new Label("Birthdate"), birthdateField,
                new Label("Gender"), genderComboBox,
                new Label("Phone"), phoneField,
                new Label("ID"), idField,
                registerButton);
        layout.setPadding(new Insets(20));

        // Set the scene and stage for registration
        Scene scene = new Scene(layout, 400, 450);
        stage.setScene(scene);
        stage.setTitle("Registration Form");
        stage.show();
    }
}