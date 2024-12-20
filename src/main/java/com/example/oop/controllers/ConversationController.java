package com.example.oop.controllers;

import com.example.oop.models.*;
import com.example.oop.utils.ConversationsManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConversationController {

    public void start(Stage stage, Conversation conversation, RegularUser user){
        List<Message> messages ;
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox messagesBox = new VBox(20);
        try {
            messages = conversation.getMessages();
            if (messages.isEmpty()) {
                Label noConversationsLabel = new Label("No Messages to display.");
                noConversationsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
                messagesBox.getChildren().add(noConversationsLabel);
            }
            else {
                for (Message message : messages) {
                    VBox messageBox = new VBox(15);
                    messageBox.setStyle("-fx-background-color: #3a3b3c; -fx-border-radius: 8; -fx-border-color: #555; -fx-border-width: 1; -fx-padding: 15;");
                    messageBox.setPrefWidth(100);

                    Label titleLabel = new Label("Sender: " + message.getSender().getName());
                    titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

                    Label contentLabel = new Label("message: " + message.getContent());
                    contentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

                    messageBox.getChildren().addAll(titleLabel,contentLabel);
                    messagesBox.getChildren().addAll(messageBox);
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        TextField messageField = new TextField();

        //Buttons
        Button backButton = new Button("Back To Conversations");
        backButton.setOnAction(e -> new ConversationListController().start(stage, user));
        backButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        Button sendButton = new Button("Send Message");
        sendButton.setOnAction(e -> {
            try {
                String messageContent = messageField.getText();
                List<Integer> receivers = new ArrayList<>();
                for(Integer part : conversation.getParticipants())
                    if(part != user.getId())receivers.add(part);
                Message message = new Message(user,receivers,messageContent);
                conversation.addMessage(message);
                ConversationsManager.updateConversation(conversation);
                message.createNotification();
                start(stage,conversation,user);
            }catch (NullPointerException ex){
                new Alert(Alert.AlertType.ERROR, "Message Field is empty!").show();
            }catch (Exception ex){
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        scrollPane.setContent(messagesBox);

        sendButton.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        VBox BottomButtons = new VBox(20);
        BottomButtons.getChildren().addAll(messageField,sendButton, backButton);
        //VBox
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(scrollPane, BottomButtons);
        layout.setStyle("-fx-background-color: #18191A;");
        //Scene
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.setTitle(user.getName() + " Conversation");
        //stage
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        stage.setWidth(screenWidth * 0.95);
        stage.setHeight(screenHeight * 0.95);
        stage.show();
    }

}