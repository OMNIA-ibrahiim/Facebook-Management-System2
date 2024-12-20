package com.example.oop.utils;

import com.example.oop.models.Comment;
import com.example.oop.models.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommentManager {

    private static final Gson gson = new Gson();
    private static final String Comments_FILE_PATH = "comments.json";
    private static final String Comments_FILE_NAME = "comments.json";
    private static final String Replies_FILE_PATH = "replies.json";
    private static final String Replies_FILE_NAME = "replies.json";

    public static int getNextCommentId() {
        List<Comment> comments = readComments();
        return comments.size();
    }

    public static List<Comment> readComments() {
        try {
            File file = new File(Comments_FILE_NAME);
            if (!file.exists())
                return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Comment>>() {}.getType();
            List<Comment> Comments = gson.fromJson(reader, listType);
            reader.close();
            return Comments != null ? Comments : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void savecomment(Comment comment) {
        List<Comment> Comments = readComments();
        Comments.add(comment);

        try (Writer writer = new FileWriter(Comments_FILE_PATH)) {
            gson.toJson(Comments, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static int getNextReplyId() {
        List<Comment> replies = readReplies();
        return ((replies.isEmpty() ? 0 : replies.size()) + 1);
    }

    public static List<Comment> readReplies() {
        try {
            File file = new File(Replies_FILE_NAME);
            if (!file.exists())
                return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Comment>>() {}.getType();
            List<Comment> reply = gson.fromJson(reader, listType);
            reader.close();
            return reply != null ? reply : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveReply(Comment Reply) {
        List<Comment> replies = readReplies();
        replies.add(Reply);
        try (Writer writer = new FileWriter(Replies_FILE_PATH)) {
            gson.toJson(replies, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static void updateComment(Comment comment) {
        List<Comment> comments = readComments();

        for (int i = 0; i < comments.size(); i++)
            if (comments.get(i).getId() == comment.getId()) {
                comments.set(i, comment);
                break;
            }
        try (Writer writer = new FileWriter(Comments_FILE_PATH)) {
            gson.toJson(comments, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

}