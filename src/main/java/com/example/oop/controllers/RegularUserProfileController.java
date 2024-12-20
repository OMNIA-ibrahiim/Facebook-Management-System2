package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegularUserProfileController {

    public void start(Stage stage, RegularUser user) {

        VBox mainContainer=new VBox(10);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));

        Label userNameLabel =new Label("Welcome,"+user.getName());
        userNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");

        mainContainer.getChildren().add(userNameLabel);

        Label SearchLabel=new Label("Search for Users by Name");
        SearchLabel.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");

        TextField searchField=new TextField();
        searchField.setPromptText("Enter a name");
        ListView<String>userListView=new ListView<>();
        userListView.setPrefHeight(70);
        searchField.textProperty().addListener((observable,oldValue,newValue)->{
            updateUserList(userListView,newValue,stage,user);
        });
        VBox search=new VBox(10);
        search.setAlignment(Pos.TOP_CENTER);
        search.getChildren().addAll(SearchLabel,searchField,userListView);
        mainContainer.getChildren().add(search);

        Button InformationButton =new Button("Information");
        InformationButton.setOnAction(e->showInfoUser(stage, user));
        InformationButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button viewYourFriendsButton=new Button("Your Friends");
        viewYourFriendsButton.setOnAction(e -> ShowAllFriends(stage,user));
        viewYourFriendsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");


        Button TimelineButton=new Button("Timeline");
        TimelineButton.setOnAction(e->new TimelineController().start(stage,user));
        TimelineButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");


        Button CreatePostButton=new Button("CreatePost");
        CreatePostButton.setOnAction(e->new CreatePostController().start(stage, user));
        CreatePostButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button conversationButton=new Button("Conversatoins");
        conversationButton.setOnAction(e ->new ConversationListController().start(stage, user));
        conversationButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button notificationsButton = new Button("Notifications");
        notificationsButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        notificationsButton.setOnAction(e -> {
            new NotificationController().start(stage,user);
            user.setHasNewNotification(false);
        });
        StackPane notificationButtonContainer = new StackPane();
        Circle redDot = new Circle(5, Color.RED);
        if(user.HasNewNotification()) {
            redDot.setVisible(true);
        }
        else
            redDot.setVisible(false);

        notificationButtonContainer.getChildren().addAll(notificationsButton, redDot);
        notificationButtonContainer.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(redDot, Pos.TOP_LEFT);
        StackPane.setMargin(redDot, new Insets(1, 0, 0, 0));

        HBox middleButton=new HBox(50,TimelineButton, CreatePostButton,notificationButtonContainer,viewYourFriendsButton,conversationButton);
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
        showUserPosts(stage,PostContainer,user);

        mainContainer.getChildren().add(scrollPane);


        Button LogOutButton =new Button(" Logout");
        LogOutButton.setOnAction(e->new OptionsController().start(stage));
        LogOutButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        VBox BottomButton=new VBox(20);

        BottomButton.setAlignment(Pos.BOTTOM_CENTER);
        BottomButton.getChildren().add( LogOutButton);
        mainContainer.getChildren().add(BottomButton);
        mainContainer.setStyle("-fx-background-color: #18191A;");

        Scene scene=new Scene(mainContainer,300,200);
        stage.setTitle("Your Profile");
        stage.setScene(scene);
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

    private void ShowAllFriends(Stage stage,RegularUser user){
        VBox friendsContainer = new VBox(10);
        friendsContainer.setPadding(new Insets(20));
        friendsContainer.setAlignment(Pos.TOP_CENTER);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new RegularUserProfileController().start(stage,user));

        try {
            ArrayList<Integer> friendsId = user.getFriends();
            ArrayList<Integer> restrictedFriendsId = user.getRestrictedFriends();

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

                    Label userNameLabel2 = new Label("User : " + friend.getName());
                    Label useremailLabel = new Label("Email : " + friend.getEmail());
                    Label userphoneLabel = new Label("Phone : " + friend.getPhone());

                    userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel);
                    friendsContainer.getChildren().add(userBox);
                }
                for (Integer friendId : restrictedFriendsId) {
                    RegularUser friend = RegularUserManager.getUserById(friendId);
                    VBox userBox = new VBox(10);
                    userBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
                    userBox.setAlignment(Pos.TOP_LEFT);

                    Label userNameLabel2 = new Label("User : " + friend.getName());
                    Label useremailLabel = new Label("Email : " + friend.getEmail());
                    Label userphoneLabel = new Label("Phone : " + friend.getPhone());

                    userBox.getChildren().addAll(userNameLabel2, useremailLabel, userphoneLabel);
                    friendsContainer.getChildren().add(userBox);
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

    private void updateUserList(ListView<String> userListView, String searchText, Stage stage, RegularUser loggedInUser) {
        List<RegularUser>users= RegularUserManager.readUsers();
        List<String> filteredUserNames=new ArrayList<>();
        for(RegularUser user:users) {
            if (user.getName().toLowerCase().contains(searchText.toLowerCase())) {
                if (!Objects.equals(user.getId(), loggedInUser.getId())){
                    filteredUserNames.add(user.getName());
                }
            }
        }
        userListView.getItems().setAll(filteredUserNames);
        userListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedUserText = userListView.getSelectionModel().getSelectedItem();
                String selectedUserName = selectedUserText.split(" \\(")[0];

                RegularUser selectedUser = users.stream()
                        .filter(user -> user.getName().equals(selectedUserName))
                        .findFirst()
                        .orElse(null);

                if (selectedUser != null)
                    new SelectedUserProfileController().start(stage, loggedInUser, selectedUser);
            }

        });
    }

    private void showInfoUser(Stage stage,RegularUser user){
        VBox userInfoContainer=new VBox(10);
        userInfoContainer.setPadding(new Insets(20));
        userInfoContainer.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name: " + user.getName());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label birthdateLabel = new Label("Birthdate: " + user.getBirthdate());
        Label genderLabel = new Label("Gender: " + user.getGender());
        Label phoneLabel = new Label("Phone: " + user.getPhone());

        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        birthdateLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        genderLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");
        phoneLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-text-fill: white;");

        userInfoContainer.getChildren().addAll(nameLabel,emailLabel,birthdateLabel,genderLabel,phoneLabel);

        Button backButton=new Button("Back");
        backButton.setOnAction(e->new RegularUserProfileController().start(stage, user));
        userInfoContainer.getChildren().add(backButton);
        userInfoContainer.setStyle("-fx-background-color: #18191A;");
        Scene scene=new Scene(userInfoContainer);
        stage.setTitle("User information");
        stage.setScene(scene);
        stage.show();
    }

    private void showUserPosts(Stage stage,VBox postContainer, RegularUser user){
        List<Post>userposts= PostManager.getPostbyUserId(user.getId());
        if(userposts.isEmpty()){
            Label nopostslabel=new Label("No Posts to Show");
            nopostslabel.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");
            postContainer.getChildren().add( nopostslabel);
        }
        else{
            for(Post post:userposts){
                VBox postvbox=new VBox(30);
                postvbox.setPadding(new Insets(10));
                postvbox.setStyle("-fx-background-color:#242526;-fx-border-color:#3a3b3c;-fx-border-radius:5px;");
                Label postcontent=new Label(post.getContent());
                postcontent.setWrapText(true);
                postcontent.setStyle("-fx-font-size: 18px;-fx-text-fill: white;");


                Label postPrivacy=new Label("Privacy: "+post.getPrivacy());
                postPrivacy.setStyle("-fx-font-size: 18px;-fx-text-fill: gray;");

                Label postLikes=new Label("Likes: "+post.getLikes().size());
                postLikes.setStyle("-fx-font-size: 18px;-fx-text-fill: gray;");


                Button commentButton = new Button("Comment");
                commentButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
                commentButton.setOnAction(e -> new CommentController().start(stage,user,post));


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
                            taggedUsers += RegularUserManager.getUserById(userId).getName();
                            if (numberOfUsers < post.getTaggedUsers().size())
                                taggedUsers += ", ";
                            numberOfUsers++;
                        }
                    }
                }catch (Exception e){System.out.println("no tagged useres!");}
                Label taggedUsersLabel = new Label("Tagged Users: " + taggedUsers);
                taggedUsersLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");


                VBox vbox=new VBox(10);
                vbox.setPadding(new Insets(10));

                vbox.setStyle("-fx-background-color:#242526;-fx-border-color:#3a3b3c;-fx-border-radius:5px;");
                vbox.getChildren().addAll(timestampLabel,taggedUsersLabel);
                HBox box=new HBox(400,postPrivacy,postLikes,commentButton);
                box.setAlignment(Pos.CENTER);
                postvbox.getChildren().addAll( vbox,postcontent, box);
                postContainer.getChildren().add(postvbox);
            }
        }
    }
}