package com.example.oop.controllers;

import com.example.oop.models.RegularUser;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button editButton;

    private RegularUser currentUser; // The user whose profile is being viewed

    // Method to initialize the controller, populating fields with the user's info
    public void initialize(RegularUser user) {
        this.currentUser = user;

        // Display the user's info in the UI
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
    }

    // Action handler for the "Edit Profile" button
    @FXML
    private void handleEditProfile() {
        // Enable the fields for editing
        nameField.setEditable(true);
        emailField.setEditable(true);
        phoneField.setEditable(true);

        // Change the button text to "Save Changes"
        editButton.setText("Save Changes");

        // Set a new action to handle saving
        editButton.setOnAction(e -> handleSaveProfile());
    }

    // Action handler for saving the profile after editing
    private void handleSaveProfile() {
        // Retrieve the updated values from the fields
        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        // Update the current user's data (You may want to validate or confirm changes here)
        currentUser.setName(newName);
        currentUser.setEmail(newEmail);
        currentUser.setPhone(newPhone);

        // Optionally save the updated user to a file or database here
        // For example, FileManager.saveUser(currentUser); (if using FileManager to handle users)

        // Disable the fields after saving
        nameField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);

        // Change the button text back to "Edit Profile"
        editButton.setText("Edit Profile");
    }

    // Method to start the profile view
    public void start(Stage stage, RegularUser user) {
        // Initialize the profile controller with the user
        initialize(user);

        // Set up the scene for the profile view
        Scene scene = new Scene(nameField.getParent(), 400, 300); // Assuming nameField's parent layout is the root
        stage.setTitle("View Profile");
        stage.setScene(scene);
        stage.show();
    }
}
