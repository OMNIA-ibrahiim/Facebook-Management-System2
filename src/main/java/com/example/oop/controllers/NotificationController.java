package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.NotificationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class NotificationController {

    public void start(Stage stage, RegularUser user){
        //VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #18191A;");
        layout.getChildren().addAll(createNotificationsBox(user), createButtonsBox(stage,user));
        //Scene
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle(user.getName() + " Notifications");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

    private ScrollPane createNotificationsBox(RegularUser user){
        VBox notificationsBox = new VBox(10);
        notificationsBox.setPadding(new Insets(20));
        notificationsBox.setAlignment(Pos.TOP_CENTER);

        try{
            List<Notification> notifications = NotificationManager.userNotifications(user);
            if(notifications.isEmpty()) {
                Label emptyLabel = new Label("NO Notifications!");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                notificationsBox.getChildren().add(emptyLabel);
            }
            for(Notification notification : notifications){
                VBox notificationBox = new VBox(10);
                notificationBox.setAlignment(Pos.CENTER);
                notificationBox.setSpacing(10);
                notificationBox.setStyle("-fx-background-color:#242526;-fx-border-color:#3a3b3c;-fx-border-radius:5px;");
                Label content=new Label(notification.getMessage());
                content.setWrapText(true);
                content.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");
                notificationBox.getChildren().add(content);
                notificationsBox.getChildren().add(notificationBox);
            }
        }
        catch (Exception e){
            new Alert(Alert.AlertType.ERROR,  "failed to load Notifications").show();
        }
        ScrollPane scrollPane = new ScrollPane(notificationsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }

    private VBox createButtonsBox(Stage stage,RegularUser user){

        Button backProfileButton =new Button("Back To Profile");
        backProfileButton.setOnAction(e->new RegularUserProfileController().start(stage, user));
        backProfileButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button backTimelineButton =new Button("Back To Timeline");
        backTimelineButton.setOnAction(e->new TimelineController().start(stage, user));
        backTimelineButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        VBox buttonsbox = new VBox(10);
        buttonsbox.setPadding(new Insets(20));
        buttonsbox.setAlignment(Pos.CENTER);
        buttonsbox.setStyle("-fx-background-color: #18191A;");
        buttonsbox.getChildren().addAll(backTimelineButton,backProfileButton);

        return buttonsbox;
    }
}
