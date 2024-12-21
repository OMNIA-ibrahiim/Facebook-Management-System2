package com.example.oop.controllers;

import com.example.oop.models.Post;
import com.example.oop.models.RegularUser;
import com.example.oop.utils.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreatePostController {

    public void start(Stage stage, RegularUser user) {
        // VBox
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.CENTER);
        ComboBox<String> privacyComboBox = new ComboBox<>();
        privacyComboBox.setPromptText("Select the Privacy");
        privacyComboBox.getItems().addAll("Private", "Public");
        privacyComboBox.setValue("Public");
        mainContainer.setStyle("-fx-background-color: #18191A;");

        TextArea postContentArea = new TextArea();
        postContentArea.setPromptText("Write your post here...");
        postContentArea.setWrapText(true);
        postContentArea.setPrefHeight(200);

        Button tagUsersButton = new Button("Tag Users");
        tagUsersButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white; -fx-font-size: 14px;");
        ArrayList<Integer> taggedUsers = new ArrayList<>();
        tagUsersButton.setOnAction(e -> tagUsersWindow(user,taggedUsers,privacyComboBox.getValue()));

        Button submitButton = new Button("Submit Post");
        submitButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white; -fx-font-size: 14px;");
        submitButton.setOnAction(e -> {
            try {
                String content = postContentArea.getText().trim();
                String privacy = privacyComboBox.getValue();
                if ((!content.isEmpty())) {
                    createPost(user, content,privacy,taggedUsers);
                    new TimelineController().start(stage, user);
                } else{
                    throw new IllegalArgumentException("The Post Can't be Empty!");
                }
            }catch (IllegalArgumentException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        Button backProfileButton =new Button("Back To Profile");
        backProfileButton.setOnAction(e->new RegularUserProfileController().start(stage, user));
        backProfileButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white; -fx-font-size: 14px;");

        Button backTimelineButton =new Button("Back To Timeline");
        backTimelineButton.setOnAction(e->new TimelineController().start(stage, user));
        backTimelineButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white; -fx-font-size: 14px;");

        mainContainer.getChildren().addAll(postContentArea, submitButton, privacyComboBox,tagUsersButton, backTimelineButton,backProfileButton);
        // Scene
        Scene scene = new Scene(mainContainer, 400, 300);
        stage.setTitle("Create Post");
        stage.setScene(scene);
        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

    private void createPost(RegularUser user, String content, String privacy,ArrayList<Integer> taggedUsers) {
        Post newPost = new Post(content, user,PostManager.getNextPostId(), privacy,taggedUsers);
        PostManager.savePost(newPost);
        newPost.createNotification();
    }

    private void tagUsersWindow(RegularUser user,ArrayList<Integer> taggedUsers,String privacy){
        Stage smallStage = new Stage();
        VBox layout = new VBox(10);

        List<TextField> taggedUsersFields = new ArrayList<>();
        Button addUserFieldButton = new Button("Add Another Member");
        addUserFieldButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        addUserFieldButton.setOnAction(e -> {
            TextField participantField = new TextField();
            participantField.setPromptText("Enter member's name...");
            taggedUsersFields.add(participantField);
            layout.getChildren().add(participantField);
        });

        //Buttons
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> smallStage.close());
        closeButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button startButton = new Button("Tag");
        startButton.setOnAction(e -> {
            if(taggedUsersFields.isEmpty()){
                new Alert(Alert.AlertType.ERROR, "Select Users To Tag!").show();
            }
            else {
                for (TextField field : taggedUsersFields) {
                    String name = field.getText();
                    if (!name.isEmpty()) {
                        RegularUser TaggedUser = RegularUserManager.findUserByName(name);
                        String friendship = user.getFriendship(TaggedUser);
                        if (TaggedUser != null && !taggedUsers.contains(TaggedUser.getId())) {
                            if (Objects.equals(TaggedUser.getId(), user.getId()))
                                new Alert(Alert.AlertType.ERROR, "You can't tag yourself!").show();
                            else if(friendship.equals("notFriend"))
                                new Alert(Alert.AlertType.ERROR, TaggedUser.getName() + " is not a friend!").show();
                            else if (privacy.equals("Private") && friendship.equals("restricted"))
                                new Alert(Alert.AlertType.ERROR, TaggedUser.getName() + " is a restricted friend and the post is private!").show();
                            else
                                taggedUsers.add(TaggedUser.getId());
                        } else
                            new Alert(Alert.AlertType.ERROR, "User " + name + " not found or already Tagged.").show();
                    }
                }
                smallStage.close();
            }
        });
        startButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        //VBox
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(startButton, closeButton, addUserFieldButton);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout, 300, 200);
        smallStage.setScene(scene);
        smallStage.setTitle("Tag Users");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        smallStage.setWidth(screenWidth * 0.50);
        smallStage.setHeight(screenHeight * 0.50);
        smallStage.show();
    }
}

