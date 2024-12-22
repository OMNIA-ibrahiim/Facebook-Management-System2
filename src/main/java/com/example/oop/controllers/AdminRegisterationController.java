package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.AdminManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdminRegisterationController {

    public void start(Stage stage){
        TextField adminPasswordField = new PasswordField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        TextField phoneField = new TextField();
        TextField birthdateField = new TextField();
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        Button registerButton = new Button("Register");
        Button BackButton =new Button("Back");

        BackButton.setOnAction(e -> new OptionsController().start(stage));

        registerButton.setOnAction(e -> {
            String adminPassword = adminPasswordField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String birthdate = birthdateField.getText();
            String gender = genderComboBox.getValue();
            String phone = phoneField.getText();

            try {
                if(adminPassword.isEmpty())
                    throw new IllegalArgumentException("Admins Password is required!");

                else if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthdate.isEmpty() || gender == null || phone.isEmpty() )
                    throw new IllegalArgumentException("All fields are required!");

                else if (!AdminManager.validateEmail(email))
                    throw new IllegalArgumentException("Invalid email format!");

                else if (!AdminManager.validatePassword(password))
                    throw new IllegalArgumentException("Password must be at least 8 characters!");

                else if (!password.equals(confirmPassword))
                    throw new IllegalArgumentException("Passwords do not match!");

                else if (AdminManager.isDuplicateEmail(email))
                    throw new IllegalArgumentException("Email already exists!");

                else {
                    if(AdminManager.validateAdminPassword(adminPassword)) {
                        int id = AdminManager.returnNextAdminId();
                        User user = new Admin(name, email, password, birthdate, gender, phone, id);
                        Admin admin = (Admin) user;
                        new AdminProfileController().start(stage,admin);
                    }
                    else{
                        throw new IllegalArgumentException("Admin Passwords do not match!");
                    }
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
        Label adminPasswordLabel = new Label("Admin password");
        adminPasswordLabel.setTextFill(Color.WHITE);

        Label nameLabel = new Label("Name");
        nameLabel.setTextFill(Color.WHITE);

        Label emailLabel = new Label("Email");
        emailLabel.setTextFill(Color.WHITE);

        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.WHITE);

        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setTextFill(Color.WHITE);

        Label birthdateLabel = new Label("Birthdate");
        birthdateLabel.setTextFill(Color.WHITE);

        Label genderLabel = new Label("Gender");
        genderLabel.setTextFill(Color.WHITE);

        Label phoneLabel = new Label("Phone");
        phoneLabel.setTextFill(Color.WHITE);

        layout.getChildren().addAll(
                adminPasswordLabel,adminPasswordField,
                nameLabel, nameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                confirmPasswordLabel, confirmPasswordField,
                birthdateLabel, birthdateField,
                genderLabel, genderComboBox,
                phoneLabel, phoneField,
                registerButton,
                BackButton
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout,300,400);
        stage.setScene(scene);
        stage.setTitle("Registration Form");

        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }
}
