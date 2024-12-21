package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.*;
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

public class ConversationListController {

    public void start(Stage stage, RegularUser user) {
        List<Conversation> userConversations ;
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox conversationsBox = new VBox(20);

        try {
            userConversations = ConversationsManager.getConversationsbyUser(user.getId());
            if (userConversations.isEmpty()) {
                Label noConversationsLabel = new Label("No Conversations to display.");
                noConversationsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
                conversationsBox.getChildren().add(noConversationsLabel);
            }
            else {
                for (Conversation conversation : userConversations) {
                    VBox conversationBox = new VBox(15);
                    conversationBox.setStyle("-fx-background-color: #3a3b3c; -fx-border-radius: 8; -fx-border-color: #555; -fx-border-width: 1; -fx-padding: 15;");
                    conversationBox.setPrefWidth(100);

                    String participants = " ";
                    int numberOfParti = 1;
                    for (Integer userId : conversation.getParticipants()) {
                        if (numberOfParti > 4) {
                            participants += " ...";
                            break;
                        }
                        participants += Objects.requireNonNull(RegularUserManager.getUserById(userId)).getName();
                        if (numberOfParti < conversation.getParticipants().size())
                            participants += ", ";
                        numberOfParti++;
                    }

                    Label titleLabel = new Label("Conversation: " + participants);
                    titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

                    HBox actions = new HBox(15);
                    actions.setAlignment(Pos.CENTER_LEFT);

                    Button enterButton = new Button("Enter");
                    enterButton.setStyle("-fx-background-color: #555; -fx-text-fill: #1877f2; -fx-border-color: #555;");
                    enterButton.setOnAction(e -> new ConversationController().start(stage, conversation, user));

                    actions.getChildren().add(enterButton);
                    conversationBox.getChildren().addAll(titleLabel, actions);
                    conversationsBox.getChildren().addAll(conversationBox);
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        scrollPane.setContent(conversationsBox);

        //Buttons
        Button newConversationButton = new Button("Start new Conversation");
        newConversationButton.setOnAction(e -> newConversationWindow(stage, user));
        newConversationButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button backProfileButton =new Button("Back To Profile");
        backProfileButton.setOnAction(e->new RegularUserProfileController().start(stage, user));
        backProfileButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button backTimelineButton =new Button("Back To Timeline");
        backTimelineButton.setOnAction(e->new TimelineController().start(stage, user));
        backTimelineButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        VBox BottomButtons = new VBox(20);
        BottomButtons.getChildren().addAll(newConversationButton, backTimelineButton,backProfileButton);
        //VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scrollPane, BottomButtons);
        layout.setStyle("-fx-background-color: #18191A;");

        //Scene
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle(user.getName() + " Conversations");

        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

    private void newConversationWindow(Stage stage, RegularUser user) {
        Stage smallStage = new Stage();
        VBox layout = new VBox(10);

        List<TextField> participantFields = new ArrayList<>();
        Button addParticipantFieldButton = new Button("Add Another Member");
        addParticipantFieldButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        addParticipantFieldButton.setOnAction(e -> {
            TextField participantField = new TextField();
            participantField.setPromptText("Enter member's name...");
            participantFields.add(participantField);
            layout.getChildren().add(participantField);
        });

        //Buttons
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> smallStage.close());
        closeButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            if(participantFields.isEmpty()){
                new Alert(Alert.AlertType.ERROR, "Select Users To start Conversation!").show();
            }
            else {
                List<Integer> participants = new ArrayList<>();
                participants.add(user.getId());
                for (TextField field : participantFields) {
                    String name = field.getText();
                    if (!name.isEmpty()) {
                        RegularUser participant = RegularUserManager.findUserByName(name);
                        if (participant != null && !participants.contains(participant.getId())) {
                            participants.add(participant.getId());
                        }else if(participant == user ){
                            new Alert(Alert.AlertType.ERROR, "You can't message yourself!").show();
                        }else {
                            new Alert(Alert.AlertType.ERROR, "User " + name + " not found or already in the conversation.").show();
                        }
                    }
                }
                Conversation newConversation = new Conversation(ConversationsManager.getNextConversationId(), participants);
                try {
                    ConversationsManager.saveConversation(newConversation);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR,"Failed to save the group conversation. Please try again.").show();
                }
                smallStage.close();
                new ConversationController().start(stage, newConversation, user);
            }
        });
        startButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        //VBox
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(startButton, closeButton,addParticipantFieldButton);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout, 300, 200);
        smallStage.setScene(scene);
        smallStage.setTitle(user.getName() + " Conversations");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        smallStage.setWidth(screenWidth * 0.50);
        smallStage.setHeight(screenHeight * 0.50);
        smallStage.show();
    }

}