package com.example.oop.controllers;

import com.example.oop.models.Admin;
import com.example.oop.models.Post;
import com.example.oop.models.RegularUser;
import com.example.oop.utils.AdminManager;
import com.example.oop.utils.PostManager;
import com.example.oop.utils.RegularUserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class AdminProfileController {

    public void start(Stage stage, Admin admin) {
        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.getChildren().addAll(ButtonsBox(stage,admin),BannedUsersBox(admin));
        mainContainer.setStyle("-fx-background-color: #18191A;");

        //scene
        Scene scene = new Scene(mainContainer);
        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.setTitle("Profile");
        stage.setScene(scene);
        stage.show();
    }

    private VBox ButtonsBox(Stage stage,Admin admin){

        Label userNameLabel = new Label("Welcome, " + admin.getName());
        userNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        Button viewAllUsersButton = new Button("View All Users");
        viewAllUsersButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        Button informationButton = new Button("Information");
        informationButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        Button logoutButton = new Button("Log out");
        logoutButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        Button viewAllPostsButton = new Button("View All Posts");
        viewAllPostsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");


        viewAllUsersButton.setOnAction(e -> showAllUsers(stage, admin));
        informationButton.setOnAction(e -> showAdminInformation(stage,admin));
        logoutButton.setOnAction(e -> new OptionsController().start(stage));
        viewAllPostsButton.setOnAction(e -> showAllPosts(stage,admin));

        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(
                userNameLabel,
                informationButton,
                viewAllUsersButton,
                viewAllPostsButton,
                logoutButton
        );
        buttonContainer.setStyle("-fx-background-color: #18191A;");

        return buttonContainer;
    }

    private ScrollPane BannedUsersBox(Admin admin){
        VBox bannedUsersContainer = new VBox(10);
        bannedUsersContainer.setPadding(new Insets(20));
        bannedUsersContainer.setAlignment(Pos.TOP_CENTER);

        List<RegularUser> bannedUsers = AdminManager.BannedUsers();
        if(bannedUsers.isEmpty()){
            Label emptyLabel = new Label("NO Banned Users" );
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            bannedUsersContainer.getChildren().add(emptyLabel);
        }
        else {
            for (RegularUser user : bannedUsers) {
                VBox userBox = new VBox(10);
                userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                userBox.setAlignment(Pos.TOP_LEFT);

                Label userNameLabel2 = new Label("User : " + user.getName());
                Label useremailLabel = new Label("Email : " + user.getEmail());
                Label userphoneLabel = new Label("Phone : " + user.getPhone());
                Label timestampLabel = new Label("At: " + user.getBanTimestamp());
                Label reasonLabel = new Label("Because : " + user.getBanReason());
                reasonLabel.setWrapText(true);

                HBox actionButtons = new HBox(10);
                actionButtons.setAlignment(Pos.CENTER_LEFT);

                Button unbanButton = new Button("Unban ");
                unbanButton.setOnAction(e -> handleBanUnban(user, admin, unbanButton));
                unbanButton.setStyle("-fx-background-color: #e7f3ff; -fx-text-fill: #0073e6;");

                actionButtons.getChildren().addAll(unbanButton);

                userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel, timestampLabel, reasonLabel, actionButtons);
                bannedUsersContainer.getChildren().add(userBox);
            }
        }
        ScrollPane scrollPane = new ScrollPane(bannedUsersContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        return scrollPane;
    }

    private void showAllUsers(Stage stage, Admin admin) {
        VBox userContainer = new VBox(10);
        userContainer.setPadding(new Insets(20));
        userContainer.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new AdminProfileController().start(stage, admin));

        try {
            List<RegularUser> users = RegularUserManager.readUsers();
            if (users.isEmpty()) {
                Label emptyLable = new Label("NO Users Found");
                userContainer.getChildren().add(emptyLable);
            } else {
                for (RegularUser user : users) {
                    VBox userBox = new VBox(10);
                    userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    userBox.setAlignment(Pos.TOP_LEFT);

                    Label userNameLabel = new Label("Name: " + user.getName());
                    Label userEmailLabel = new Label("Email: " + user.getEmail());
                    Label userPhoneLabel = new Label("Phone: " + user.getPhone());
                    Label userStatusLabel = new Label("Status: " + (user.isBanned() ? "Banned" : "Active"));

                    Button actionButton = new Button(user.isBanned() ? "Unban" : "Ban");
                    actionButton.setStyle("-fx-background-color: #e7f3ff; -fx-text-fill: #0073e6;");
                    actionButton.setOnAction(e -> handleBanUnban(user, admin, actionButton));

                    Button removeUserButton = new Button("Remove User");
                    removeUserButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
                    removeUserButton.setOnAction(e -> {
                        admin.removeUser(user);
                        userContainer.getChildren().remove(userBox);
                    });
                    HBox actionButtons = new HBox(10);
                    actionButtons.setAlignment(Pos.CENTER_LEFT);
                    actionButtons.getChildren().addAll(actionButton, removeUserButton);

                    userBox.getChildren().addAll(userNameLabel, userEmailLabel, userPhoneLabel, userStatusLabel, actionButtons);
                    userContainer.getChildren().add(userBox);
                }
            }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }

        ScrollPane scrollPane = new ScrollPane(userContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.getChildren().addAll(scrollPane, backButton);
        mainContainer.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(mainContainer);
        //Stage
        stage.setTitle("View All Users");
        stage.setScene(scene);
        stage.show();
    }

    private void showAllPosts(Stage stage, Admin admin) {
        VBox postContainer = new VBox(10);
        postContainer.setPadding(new Insets(20));
        postContainer.setAlignment(Pos.TOP_CENTER);

        try {
            List<Post> posts = PostManager.readPosts();
            if (posts.isEmpty()) {
                Label emptyLable = new Label("NO Posts Found");
                postContainer.getChildren().add(emptyLable);
                }
            else {
                for (Post post : posts) {
                    VBox postBox = new VBox(10);
                    postBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    postBox.setAlignment(Pos.TOP_LEFT);

                    Label userNameLabel = new Label("Name: " + post.getUser().getName());
                    Label userEmailLabel = new Label("Email: " + post.getUser().getEmail());
                    Label userPhoneLabel = new Label("Phone: " + post.getUser().getPhone());
                    Label contentLabel = new Label("Phone: " + post.getContent());

                    Button actionButton = new Button(post.getUser().isBanned() ? "Unban" : "Ban");
                    actionButton.setStyle("-fx-background-color: #e7f3ff; -fx-text-fill: #0073e6;");
                    actionButton.setOnAction(e -> handleBanUnban(post.getUser(), admin, actionButton));

                    Button removeUserButton = new Button("Remove User");
                    removeUserButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
                    removeUserButton.setOnAction(e -> {
                        admin.removeUser(post.getUser());
                        showAllPosts(stage,admin);
                    });

                    Button removePostButton = new Button("Remove Post");
                    removePostButton.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white;");
                    removePostButton.setOnAction(e -> {
                        admin.removePost(post);
                        showAllPosts(stage,admin);
                    });

                    HBox actionButtons = new HBox(10);
                    actionButtons.setAlignment(Pos.CENTER_LEFT);
                    actionButtons.getChildren().addAll(actionButton, removeUserButton , removePostButton);

                    postBox.getChildren().addAll(userNameLabel, userEmailLabel, userPhoneLabel,contentLabel, actionButtons);
                    postContainer.getChildren().add(postBox);
                }
            }
        } catch (NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
        ScrollPane scrollPane = new ScrollPane(postContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new AdminProfileController().start(stage, admin);
        });

        VBox mainContainer = new VBox(10);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.getChildren().addAll(scrollPane, backButton);
        mainContainer.setStyle("-fx-background-color: #18191A;");
        //scene
        Scene scene = new Scene(mainContainer);
        //Stage
        stage.setTitle("View All Posts");
        stage.setScene(scene);
        stage.show();
    }

    private void handleBanUnban(RegularUser user, Admin admin, Button actionButton) {
        if (user.isBanned()) {
            admin.unbanUser(user);
            actionButton.setText("Ban");
        } else {
            admin.banUser(user, "Violated community guidelines.");
            actionButton.setText("Unban");
        }
    }

    private void showAdminInformation(Stage stage, Admin admin) {
        VBox infoContainer = new VBox(10);
        infoContainer.setPadding(new Insets(20));
        infoContainer.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name: " + admin.getName());
        Label emailLabel = new Label("Email: " + admin.getEmail());
        Label birthdateLabel = new Label("Birthdate: " + admin.getBirthdate());
        Label genderLabel = new Label("Gender: " + admin.getGender());
        Label phoneLabel = new Label("Phone: " + admin.getPhone());

        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        birthdateLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        genderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        phoneLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");

        infoContainer.getChildren().addAll(nameLabel, emailLabel, birthdateLabel, genderLabel, phoneLabel);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new AdminProfileController().start(stage, admin);
        });

        infoContainer.getChildren().add(backButton);
        infoContainer.setStyle("-fx-background-color: #18191A;");

        //Scene
        Scene infoScene = new Scene(infoContainer);

        //Stage
        stage.setTitle("User Information");
        stage.setScene(infoScene);
        stage.show();
    }
}