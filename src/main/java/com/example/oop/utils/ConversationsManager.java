package com.example.oop.utils;

import com.example.oop.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConversationsManager {

    private static final Gson gson = new Gson();

    private static final String CONVERSATIONS_FILE_PATH = "CONVERSATIONs.json";
    private static final String CONVERSATIONS_FILE_NAME = "CONVERSATIONs.json";

    public static List<Conversation> readConversations() {
        try {
            File file = new File(CONVERSATIONS_FILE_NAME);
            if (!file.exists())
                return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Conversation>>() {
            }.getType();
            List<Conversation> conversations = gson.fromJson(reader, listType);
            reader.close();

            return conversations != null ? conversations : new ArrayList<>();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return new ArrayList<>();
        }
    }

    public static void saveConversation(Conversation conversation) {
        List<Conversation> conversations = readConversations();
        conversations.add(conversation);
        try (Writer writer = new FileWriter(CONVERSATIONS_FILE_PATH)) {
            gson.toJson(conversations, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static int getNextConversationId() {
        try {
            List<Conversation> conversations = readConversations();
            return ((conversations.isEmpty() ? 0 : conversations.size()) + 1);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return 0;
        }
    }

    public static List<Conversation> getConversationsbyUser(int userid){
        List<Conversation> userConversations = new ArrayList<>();
        for(Conversation Conversation : readConversations()){
            if(Conversation.getParticipants().contains(userid))
                userConversations.add(Conversation);
        }
        return userConversations;
    }

    public static void updateConversation(Conversation conversation) {
        List<Conversation> conversations = readConversations();

        for (int i = 0; i < conversations.size(); i++)
            if (Objects.equals(conversations.get(i).getId(), conversation.getId())) {
                conversations.set(i, conversation);
                break;
            }

        try (Writer writer = new FileWriter(CONVERSATIONS_FILE_PATH)) {
            gson.toJson(conversations, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

}