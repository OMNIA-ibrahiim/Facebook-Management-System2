package com.example.oop.controllers;
import com.example.oop.utils.CommentManager;
import com.example.oop.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentController {

    public void start(Stage stage, RegularUser user, Post post) {

        VBox Container = new VBox(10);
        Container.setStyle("-fx-background-color: #242526; -fx-border-radius: 8; " +
                "-fx-border-color: #555; -fx-padding: 10;");
        Container.setPrefWidth(580);

        Label header = new Label("Comments");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        VBox commentsContainer = new VBox(5);
        commentsContainer.setStyle("-fx-padding: 5; -fx-background-color: #3a3b3c; -fx-border-radius: 5;");

        List<Comment> commentList = CommentManager.readComments();
        List<Comment> comm = commentList.stream()
                .filter(comment -> comment.getUser() != null && comment.getPostId() == post.getId()  )
                .toList();

        for (Comment comment : comm) {
            VBox COMMENT = createCommentBox(comment, post, user);
            commentsContainer.getChildren().add(COMMENT);
        }
        scrollPane.setContent(commentsContainer);

        TextField comment_Input = new TextField();
        comment_Input.setPromptText("Write a comment...");
        comment_Input.setStyle("-fx-background-color: #555; -fx-font-size:14px; -fx-text-fill: white;" +
                " -fx-border-radius: 5; -fx-padding: 10;");

        Button submitCommentButton = new Button("Post Comment");
        submitCommentButton.setStyle("-fx-background-color: #1d81ba; -fx-text-fill: #ffffff; " +
                "-fx-border-radius: 10; -fx-font-size: 15px;");

        Label confirmationLabel = new Label();
        submitCommentButton.setOnAction(e -> {
            String commentText = comment_Input.getText().trim();
            if (!commentText.isEmpty()) {
                Comment newComment = new Comment(CommentManager.getNextCommentId(),post, user, commentText);
                if(!Objects.equals(post.getUser().getId(), user.getId()))
                    newComment.createNotification();
                CommentManager.savecomment(newComment);
                VBox COMMENT = createCommentBox(newComment, post, user);
                commentsContainer.getChildren().add(COMMENT);
                comment_Input.clear();
                Container.getChildren().remove(confirmationLabel);

            }
            else {
                confirmationLabel.setText("Please write a comment before clicking the button.");
                confirmationLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");

            }

        });

        Button backToPostButton = new Button("Back To Posts");
        backToPostButton.setStyle("-fx-background-color: #969191; -fx-text-fill: #ffffff; -fx-border-radius: 10; " +
                "-fx-font-size: 15px;");
        backToPostButton.setOnAction(e -> {
            new TimelineController().start(stage,user);
        });

        HBox Buttons = new HBox(10, comment_Input,submitCommentButton, backToPostButton);
        Buttons.setAlignment(Pos.CENTER_LEFT);

        Container.getChildren().addAll(header, scrollPane, Buttons,confirmationLabel);
        Scene scene = new Scene(Container, 800, 600);
        stage.setTitle("Comments");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createCommentBox(Comment comment, Post post, RegularUser user) {
        VBox commentBox = new VBox(10);
        commentBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");
        commentBox.setPadding(new Insets(10));

        Label commentAuthor = new Label(comment.getUser().getName() + " says:");
        commentAuthor.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size:16px;");

        Label commentContent = new Label("\"" + comment.getContent() + "\"");
        commentContent.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size:15px;");

        Label timestampLabel = new Label("At: " + comment.getTimestamp());
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");

        HBox actionButtons = new HBox(10);

        Button replyButton = new Button("Reply");
        replyButton.setStyle("-fx-background-color: #1d81ba; -fx-text-fill: white; -fx-font-size:15px;" +
                " -fx-border-radius: 5;");

        replyButton.setOnAction(e -> {
            TextField replyField = new TextField();
            replyField.setPromptText("Write your reply...");

            Button submitReplyButton = new Button("Submit Reply");
            submitReplyButton.setStyle("-fx-background-color: #1d81ba; -fx-text-fill: white; -fx-font-size:15px;" +
                    " -fx-border-radius: 5;");

            VBox replyBox = new VBox(5, replyField, submitReplyButton);
            replyBox.setStyle("-fx-padding: 10;");

            submitReplyButton.setOnAction(ev -> {
                String replyText = replyField.getText().trim();
                if (!replyText.isEmpty()) {
                    Comment newReply = new Comment(CommentManager.getNextReplyId(),post, user, replyText);
                    newReply.setId(comment.getId());
                    CommentManager.saveReply(newReply);
                    replyField.clear();
                    replyBox.getChildren().clear();

                }
            });

            commentBox.getChildren().addAll(replyBox);
        });

        Button showRepliesButton = new Button("Show Replies");
        showRepliesButton.setStyle("-fx-background-color: #1d81ba; -fx-text-fill: white; -fx-font-size:15px; " + "-fx-border-radius: 5;");
        VBox repliesContainer = new VBox(5);

        showRepliesButton.setOnAction(e -> {
            repliesContainer.getChildren().clear();
            List<Comment> replies = CommentManager.readReplies().stream()
                    .filter(reply -> reply.getId() == comment.getId())
                    .toList();

            for (Comment reply : replies) {
                VBox REPLY = createReplyBox(reply);
                repliesContainer.getChildren().add(REPLY);
            }
        });

        Button likeButton = new Button();
        try {
            if (comment.getLikes().contains(user.getId()))
                likeButton.setText("Unlike");
            else
                likeButton.setText("Like");
        }catch (Exception e){
            likeButton.setText("Like");
        }
        likeButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
        Tooltip tooltip = new Tooltip(comment.getLikeCounter() +" Likes");
        tooltip.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: black;");
        likeButton.setTooltip(tooltip);
        likeButton.setOnAction(e -> {
            try {
                ArrayList<Integer> commentLikes = comment.getLikes();
                if(commentLikes.contains(user.getId())){
                    comment.removeLike(user.getId());
                    likeButton.setText("Like");
                }
                else{
                    if(Objects.equals(comment.getUser().getId(), user.getId())){
                        new Alert(Alert.AlertType.ERROR, "You can't like your Comments!").show();
                    }
                    else {
                        comment.addLike(user.getId());
                        likeButton.setText("Unlike");
                        comment.createNotification(user);
                    }
                }
                CommentManager.updateComment(comment);
                actionButtons.getChildren().removeAll(likeButton,replyButton, showRepliesButton);
                actionButtons.getChildren().addAll(likeButton,replyButton, showRepliesButton);
                tooltip.setText(comment.getLikeCounter() +" Likes");
            }catch (Exception ex){
                new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            }
        });
        actionButtons.getChildren().addAll(likeButton,replyButton, showRepliesButton);
        commentBox.getChildren().addAll(commentAuthor,timestampLabel, commentContent, actionButtons, repliesContainer);

        return commentBox;
    }

    private VBox createReplyBox(Comment reply) {
        VBox replyBox = new VBox(5);
        replyBox.setStyle("-fx-padding: 5; -fx-background-color: #555; -fx-border-radius: 5;");

        Label replyAuthor = new Label(reply.getUser().getName() + " replies:");
        replyAuthor.setStyle("-fx-font-weight: bold; -fx-text-fill:white; -fx-font-size:14px;");

        Label replyContent = new Label("\"" + reply.getContent() + "\"");
        replyContent.setStyle("-fx-text-fill:white; -fx-font-size:14px;");

        Label timestampLabel = new Label("At: " + reply.getTimestamp());
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #aaa;");

        replyBox.getChildren().addAll(replyAuthor,timestampLabel ,replyContent);
        return replyBox;
    }

}