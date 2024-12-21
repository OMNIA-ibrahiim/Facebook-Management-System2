package com.example.oop.controllers;

import com.example.oop.models.Post;
import com.example.oop.models.RegularUser;
import com.example.oop.utils.PostManager;
import com.example.oop.utils.RegularUserManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectedUserProfileController {

    public void start(Stage stage , RegularUser loggedInUser, RegularUser selectedUser){
        VBox mainContainer=new VBox(10);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));

        Label userNameLabel =new Label(selectedUser.getName());
        userNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");

        mainContainer.getChildren().add(userNameLabel);

        Button InformationButton =new Button("Information");
        InformationButton.setOnAction(e -> showInfoUser(stage,loggedInUser, selectedUser));
        InformationButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button viewUserFriendsButton=new Button(selectedUser.getName()+" Friends");
        viewUserFriendsButton.setOnAction(e-> showAllFriends(stage,loggedInUser,selectedUser));
        viewUserFriendsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button friendshipButton =new Button("Friendship");
        friendshipButton.setOnAction(e-> handleFriendshipButton(stage,loggedInUser,selectedUser));
        friendshipButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button backButton =new Button("Back To Your Profile");
        backButton.setOnAction(e->new RegularUserProfileController().start(stage,loggedInUser));
        backButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        VBox BottomButton=new VBox(20);

        BottomButton.setAlignment(Pos.BOTTOM_CENTER);
        BottomButton.getChildren().add( backButton);

        HBox middleButton=new HBox(50,friendshipButton,viewUserFriendsButton);
        middleButton.setAlignment(Pos.CENTER);

        VBox buttons=new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.getChildren().addAll(InformationButton,middleButton);
        mainContainer.getChildren().add(buttons);

        VBox PostContainer = new VBox(20);
        PostContainer.setPadding(new Insets(20));
        PostContainer.setAlignment(Pos.TOP_CENTER);


        ScrollPane scrollPane = new ScrollPane(PostContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        showUserPosts(stage,PostContainer,loggedInUser,selectedUser);

        mainContainer.getChildren().add(scrollPane);
        mainContainer.getChildren().add(BottomButton);
        mainContainer.setStyle("-fx-background-color: #18191A;");

        //Scene
        Scene scene=new Scene(mainContainer,300,200);
        //Stage
        stage.setTitle("Your Profile");
        stage.setScene(scene);
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();

    }

    private void handleFriendshipButton(Stage stage,RegularUser loggedInUser,RegularUser selectedUser){
        Button showMutualPostsButton = new Button("Show Mutual Posts");
        Button showMutualFriendsButton = new Button("Show Mutual Friends");
        Button confirmButton = new Button("Confirm Friendship Selection");
        Button backButton = new Button("Back");
        ComboBox<String> FriendshipComboBox = new ComboBox<>();

        FriendshipComboBox.setPromptText("Choose Type of Friendship");
        FriendshipComboBox.getItems().addAll("Normal Friend", "Restricted Friend", "Not Friend");
        String relationType = loggedInUser.getFriendship(selectedUser);
        if(relationType.equals("notFriend"))
            FriendshipComboBox.setValue("Not Friend");
        else if(relationType.equals("normal"))
            FriendshipComboBox.setValue("Normal Friend");
        else
            FriendshipComboBox.setValue("Restricted Friend");

        FriendshipComboBox.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        showMutualPostsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        showMutualFriendsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        confirmButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        backButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        showMutualFriendsButton.setOnAction(e -> showMutualFriends(stage,loggedInUser,selectedUser));
        showMutualPostsButton.setOnAction(e -> showMutualPosts(stage,loggedInUser,selectedUser));
        confirmButton.setOnAction(e ->{
            String selection = FriendshipComboBox.getValue();
            if(selection.equals("Restricted Friend") || selection.equals("Normal Friend"))
                loggedInUser.addFriend(selectedUser,selection);
            else loggedInUser.removeFriend(selectedUser);
        });
        backButton.setOnAction(e -> new SelectedUserProfileController().start(stage,loggedInUser,selectedUser));

        //VBox
        VBox layout =new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(FriendshipComboBox,confirmButton,showMutualFriendsButton,showMutualPostsButton,backButton);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene=new Scene(layout,300,200);
        stage.setScene(scene);
        stage.setTitle("Friendship Page");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();

    }

    private void showMutualPosts(Stage stage,RegularUser loggedInUser,RegularUser selectedUser){
        VBox postsContainer = new VBox(10);
        postsContainer.setPadding(new Insets(20));
        postsContainer.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleFriendshipButton(stage,loggedInUser,selectedUser));

        try {
            ArrayList<Post> mutualPosts = loggedInUser.mutualPosts(selectedUser);
            if (mutualPosts.isEmpty()) {
                Label emptyLabel = new Label("NO Mutual Posts");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                postsContainer.getChildren().add(emptyLabel);
            } else {
                for (Post post : mutualPosts) {
                    VBox postBox = new VBox(10);
                    postBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    postBox.setAlignment(Pos.TOP_LEFT);

                    Label postContentLabel = new Label( post.getContent());

                    postBox.getChildren().addAll(postContentLabel);
                    postsContainer.getChildren().add(postBox);
                }
            }
            ScrollPane scrollPane = new ScrollPane(postsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            VBox mainContainer = new VBox(10);
            mainContainer.setPadding(new Insets(20));
            mainContainer.setAlignment(Pos.TOP_CENTER);
            mainContainer.setStyle("-fx-background-color: #18191A;");
            mainContainer.getChildren().addAll(scrollPane, backButton);
            //scene
            Scene scene = new Scene(mainContainer);
            //Stage
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            stage.setWidth(screenWidth * 0.95);
            stage.setHeight(screenHeight * 0.95);
            stage.setTitle("Mutual Posts");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    private void showMutualFriends(Stage stage,RegularUser loggedInUser,RegularUser selectedUser){
        VBox friendsContainer = new VBox(10);
        friendsContainer.setPadding(new Insets(20));
        friendsContainer.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> handleFriendshipButton(stage,loggedInUser,selectedUser));

        try {
            ArrayList<Integer> mutualFriendsId = loggedInUser.mutualFriends(selectedUser);
            if (mutualFriendsId.isEmpty()) {
                Label emptyLabel = new Label("NO Mutual Freinds");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                friendsContainer.getChildren().add(emptyLabel);
            } else {
                for (Integer friendId : mutualFriendsId) {
                    RegularUser friend = RegularUserManager.getUserById(friendId);
                    VBox userBox = new VBox(10);
                    userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    userBox.setAlignment(Pos.TOP_LEFT);

                    if(friend != null) {
                        Label userNameLabel2 = new Label("User : " + friend.getName());
                        Label useremailLabel = new Label("Email : " + friend.getEmail());
                        Label userphoneLabel = new Label("Phone : " + friend.getPhone());
                        userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel);
                        friendsContainer.getChildren().add(userBox);
                    }
                }
            }
            ScrollPane scrollPane = new ScrollPane(friendsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            VBox mainContainer = new VBox(10);
            mainContainer.setPadding(new Insets(20));
            mainContainer.setAlignment(Pos.TOP_CENTER);
            mainContainer.setStyle("-fx-background-color: #18191A;");
            mainContainer.getChildren().addAll(scrollPane, backButton);

            //scene
            Scene scene = new Scene(mainContainer);

            //Stage
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            stage.setWidth(screenWidth * 0.95);
            stage.setHeight(screenHeight * 0.95);
            stage.setTitle("Mutual Friends");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    private void showAllFriends(Stage stage,RegularUser loggedInUser,RegularUser selectedUser){
        VBox friendsContainer = new VBox(10);
        friendsContainer.setPadding(new Insets(20));
        friendsContainer.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new SelectedUserProfileController().start(stage,loggedInUser,selectedUser));

        try {
            ArrayList<Integer> friendsId = selectedUser.getFriends();
            ArrayList<Integer> restrictedFriendsId = selectedUser.getRestrictedFriends();

            if (friendsId.isEmpty() && restrictedFriendsId.isEmpty()) {
                Label emptyLabel = new Label("NO Freinds");
                emptyLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                friendsContainer.getChildren().add(emptyLabel);
            } else {
                for (Integer friendId : friendsId) {
                    RegularUser friend = RegularUserManager.getUserById(friendId);
                    VBox userBox = new VBox(10);
                    userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    userBox.setAlignment(Pos.TOP_LEFT);

                    if(friend != null) {
                        Label userNameLabel2 = new Label("User : " + friend.getName());
                        Label useremailLabel = new Label("Email : " + friend.getEmail());
                        Label userphoneLabel = new Label("Phone : " + friend.getPhone());
                        userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel);
                        friendsContainer.getChildren().add(userBox);
                    }
                }
                for (Integer friendId : restrictedFriendsId) {
                    RegularUser friend = RegularUserManager.getUserById(friendId);
                    VBox userBox = new VBox(10);
                    userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    userBox.setAlignment(Pos.TOP_LEFT);

                    if(friend != null) {
                        Label userNameLabel2 = new Label("User : " + friend.getName());
                        Label useremailLabel = new Label("Email : " + friend.getEmail());
                        Label userphoneLabel = new Label("Phone : " + friend.getPhone());
                        userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel);
                        friendsContainer.getChildren().add(userBox);
                    }
                }
            }
            ScrollPane scrollPane = new ScrollPane(friendsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

            VBox mainContainer = new VBox(10);
            mainContainer.setPadding(new Insets(20));
            mainContainer.setAlignment(Pos.TOP_CENTER);
            mainContainer.setStyle("-fx-background-color: #18191A;");
            mainContainer.getChildren().addAll(scrollPane, backButton);
            //scene
            Scene scene = new Scene(mainContainer);
            //Stage
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();
            stage.setWidth(screenWidth * 0.95);
            stage.setHeight(screenHeight * 0.95);
            stage.setTitle("Mutual Friends");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    private void showInfoUser(Stage stage,RegularUser loggedInUser,RegularUser selectedUser){
        VBox userInfoContainer=new VBox(10);
        userInfoContainer.setPadding(new Insets(20));
        userInfoContainer.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name: " + selectedUser.getName());
        Label emailLabel = new Label("Email: " + selectedUser.getEmail());
        Label birthdateLabel = new Label("Birthdate: " + selectedUser.getBirthdate());
        Label genderLabel = new Label("Gender: " + selectedUser.getGender());
        Label phoneLabel = new Label("Phone: " + selectedUser.getPhone());

        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        birthdateLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        genderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        phoneLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");

        userInfoContainer.getChildren().addAll(nameLabel,emailLabel,birthdateLabel,genderLabel,phoneLabel);

        Button backButton=new Button("Back");
        backButton.setOnAction(e->new SelectedUserProfileController().start(stage, loggedInUser,selectedUser));
        userInfoContainer.getChildren().add(backButton);
        userInfoContainer.setStyle("-fx-background-color: #18191A;");
        Scene scene=new Scene(userInfoContainer);
        stage.setTitle("User information");
        stage.setScene(scene);
        stage.show();
    }

    private void showUserPosts(Stage stage,VBox postContainer,RegularUser loggedInUser, RegularUser selectedUser){
        List<Post> userposts= PostManager.getPostbyUserId(selectedUser.getId());
        if(userposts.isEmpty()){
            Label nopostslabel=new Label("No Posts to Show");
            nopostslabel.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");
            postContainer.getChildren().add( nopostslabel);
        }
        else{
            for(Post post:userposts){
                String privacy = post.getPrivacy();
                String relation = loggedInUser.getFriendship(selectedUser);
                if((relation.equals("restricted") || relation.equals("notFriend")) && privacy.equals("Private"))
                    continue;
                VBox postvbox=new VBox(10);
                postvbox.setPadding(new Insets(10));
                postvbox.setStyle("-fx-background-color:#242526;-fx-border-color:#3a3b3c;-fx-border-radius:5px;");
                Label postcontent=new Label(post.getContent());
                postcontent.setWrapText(true);
                postcontent.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");

                Label postPrivacy=new Label("Privacy: "+post.getPrivacy());
                postPrivacy.setStyle("-fx-font-size: 18px;-fx-text-fill: gray;");


                Button commentButton = new Button("Comment");
                commentButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
                commentButton.setOnAction(e -> new CommentController().start(stage,loggedInUser,post));

                Label timestampLabel = new Label("At: " + post.getTimestamp());
                timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");

                String taggedUsers = "No Tagged Users";
                try {
                    if(!post.getTaggedUsers().isEmpty()) {
                        taggedUsers = "" ;
                        int numberOfUsers = 1;
                        for (Integer userId : post.getTaggedUsers()) {
                            if (numberOfUsers > 4) {
                                taggedUsers += " ...";
                                break;
                            }
                            taggedUsers += Objects.requireNonNull(RegularUserManager.getUserById(userId)).getName();
                            if (numberOfUsers < post.getTaggedUsers().size())
                                taggedUsers += ", ";
                            numberOfUsers++;
                        }
                    }
                }catch (Exception e){System.out.println("no tagged useres!");}
                Label taggedUsersLabel = new Label("Tagged Users: " + taggedUsers);
                taggedUsersLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");
                HBox box=new HBox(400);

                Button likeButton = new Button();
                try {
                    if (post.getLikes().contains(loggedInUser.getId()))
                        likeButton.setText("Unlike");
                    else
                        likeButton.setText("Like");
                }catch (Exception e){
                    likeButton.setText("Like");
                }
                likeButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
                Tooltip tooltip = new Tooltip(post.getLikeCounter() +" Likes");
                tooltip.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: black;");
                likeButton.setTooltip(tooltip);
                likeButton.setOnAction(e -> {
                    try {
                        ArrayList<Integer> postLikes = post.getLikes();
                        if(postLikes.contains(loggedInUser.getId())){
                            post.removeLike(loggedInUser.getId());
                            likeButton.setText("Like");
                        }
                        else{
                            if(Objects.equals(post.getUser().getId(), loggedInUser.getId())){
                                new Alert(Alert.AlertType.ERROR, "You can't like your posts!").show();

                            }
                            else {
                                post.addLike(loggedInUser.getId());
                                likeButton.setText("Unlike");
                                post.createNotification(selectedUser,loggedInUser);
                            }
                        }
                        PostManager.updatePost(post);
                        box.getChildren().removeAll(postPrivacy,likeButton,commentButton);
                        box.getChildren().addAll(postPrivacy,likeButton,commentButton);
                        tooltip.setText(post.getLikeCounter() +" Likes");
                    }catch (Exception ex){
                        System.out.println("Error in likes!");
                    }
                });

                VBox vbox=new VBox(10);
                vbox.setPadding(new Insets(10));

                vbox.setStyle("-fx-background-color:#242526;-fx-border-color:#3a3b3c;-fx-border-radius:5px;");
                vbox.getChildren().addAll(timestampLabel,taggedUsersLabel);
                box.getChildren().addAll(postPrivacy,likeButton,commentButton);
                box.setAlignment(Pos.CENTER);
                postvbox.getChildren().addAll( vbox,postcontent, box);
                postContainer.getChildren().add(postvbox);
            }
        }
    }
}
