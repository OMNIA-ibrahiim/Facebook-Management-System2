package com.example.oop.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OptionsController {

    public void start(Stage stage){
        // Buttons
        Button registerRegularUserButton =new Button("Register As RegularUser ");
        Button registerAdminButton =new Button("Register AS Admin");
        Button loginRegularUserButton =new Button("LogIn AS RegularUser");
        Button loginAdminButton =new Button("LogIn AS Admin");

        registerAdminButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        registerRegularUserButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        loginAdminButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        loginRegularUserButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        registerRegularUserButton.setOnAction(e -> new UserRegistrationController().start(stage) );
        registerAdminButton.setOnAction(e -> new AdminRegisterationController().start(stage) );
        loginRegularUserButton.setOnAction(e->new UserLoginController().start(stage));
        loginAdminButton.setOnAction(e->new  AdminLoginController().start(stage));

        //layout
        VBox layout =new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll( registerRegularUserButton, loginRegularUserButton, loginAdminButton,registerAdminButton);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout,300,400);
        stage.setScene(scene);
        stage.setTitle("Choose an Option");
        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

}