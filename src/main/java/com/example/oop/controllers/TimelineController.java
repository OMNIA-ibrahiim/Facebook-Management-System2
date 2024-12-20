package com.example.oop.controllers;


import com.example.oop.models.*;
import com.example.oop.utils.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.*;

import java.util.ArrayList;
import java.util.List;

public class TimelineController {

    public void start(Stage stage, RegularUser user) {
        HBox mainContainer = new HBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #18191A;");

        VBox leftContainer = createLeftContainer(stage, user);
        VBox centerContainer = createCenterContainer(stage,user);
        mainContainer.getChildren().addAll(leftContainer, centerContainer);

        //Scene
        Scene scene = new Scene(mainContainer);
        //Stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.setTitle("Timeline");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLeftContainer(Stage stage, RegularUser user) {
        VBox leftContainer = new VBox(20);
        leftContainer.setPadding(new Insets(20));
        leftContainer.setAlignment(Pos.TOP_LEFT);
        leftContainer.setStyle("-fx-background-color: #242526; -fx-border-right: 10px solid #3a3b3c;"); // Dark gray background for left container
        leftContainer.setMinWidth(250);

        Button viewProfileButton = new Button("View Profile");
        viewProfileButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3a3b3c; -fx-text-fill: white; -fx-border-color: #3a3b3c;");
        viewProfileButton.setOnAction(e -> new RegularUserProfileController().start(stage, user));

        Button conversationButton=new Button("Conversatoins");
        conversationButton.setOnAction(e ->new ConversationListController().start(stage, user));
        conversationButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3a3b3c; -fx-text-fill: white; -fx-border-color: #3a3b3c;");

        Button notificationsButton = new Button("Notifications");
        notificationsButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3a3b3c; -fx-text-fill: white; -fx-border-color: #3a3b3c;");
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

        Button createPostButton = new Button("Create Post");
        createPostButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3a3b3c; -fx-text-fill: white; -fx-border-color: #3a3b3c;");
        createPostButton.setOnAction(e -> new CreatePostController().start(stage, user));

        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3a3b3c; -fx-text-fill: #1877f2; -fx-border-color: #3a3b3c;");
        logoutButton.setOnAction(e -> new OptionsController().start(stage));

        leftContainer.getChildren().addAll(viewProfileButton, notificationButtonContainer,conversationButton, createPostButton, logoutButton);
        return leftContainer;
    }

    private VBox createCenterContainer(Stage stage,RegularUser user) {
        VBox centerContainer = new VBox(20);
        centerContainer.setPadding(new Insets(20));
        centerContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);

        Label header = new Label("Friends' Posts");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox postsContainer = new VBox(20);
        postsContainer.setPadding(new Insets(10));
        postsContainer.setAlignment(Pos.TOP_CENTER);

        List<Post> posts = PostManager.readPosts();

        if (posts.isEmpty()) {
            Label noPostsLabel = new Label("No posts to display.");
            noPostsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            postsContainer.getChildren().add(noPostsLabel);
        }

        for (Post post : posts) {
            String privacy = post.getPrivacy();
            String relation = user.getFriendship(post.getUser());
            if((relation.equals("restricted") && privacy.equals("Private"))|| relation.equals("notFriend"))
                continue;
            VBox postBox = createPostBox(stage, post, user);
            postsContainer.getChildren().add(postBox);
        }

        scrollPane.setContent(postsContainer);
        centerContainer.getChildren().addAll(header, scrollPane);
        return centerContainer;
    }

    private VBox createPostBox(Stage stage,Post post,RegularUser user) {
        VBox postBox = new VBox(15);
        postBox.setStyle("-fx-background-color: #3a3b3c; -fx-border-radius: 8; -fx-border-color: #555; -fx-border-width: 1; -fx-padding: 15;");
        postBox.setPrefWidth(600);

        Label authorLabel = new Label("Posted by: " + post.getUser().getName());
        authorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

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
        }catch (Exception e){new Alert(Alert.AlertType.ERROR, "Error Happened").show();}
        Label taggedUsersLabel = new Label("Tagged Users: " + taggedUsers);
        taggedUsersLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");

        Label contentLabel = new Label(post.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button commentButton = new Button("Comment");
        commentButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
        commentButton.setOnAction(e -> new CommentController().start(stage,user,post));

        Button likeButton = new Button();
        try {
            if (post.getLikes().contains(user.getId()))
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
                if(postLikes.contains(user.getId())){
                    post.removeLike(user.getId());
                    likeButton.setText("Like");
                }
                else{
                    if(post.getUser().getId() == user.getId())
                        new Alert(Alert.AlertType.ERROR, "You can't like your posts!").show();
                    else {
                        post.addLike(user.getId());
                        likeButton.setText("Unlike");
                        post.createLikeNotification(post.getUser(),user);
                    }
                }
                PostManager.updatePost(post);
                actions.getChildren().removeAll(likeButton,commentButton);
                actions.getChildren().addAll(likeButton,commentButton);
                tooltip.setText(post.getLikeCounter() +" Likes");
            }catch (Exception ex){
                new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            }
        });

        actions.getChildren().addAll(likeButton, commentButton);
        postBox.getChildren().addAll(authorLabel, timestampLabel,taggedUsersLabel, contentLabel, actions);

        return postBox;
    }

}