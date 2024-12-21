package com.example.oop.utils;

import com.example.oop.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PostManager {
    private static final Gson gson = new Gson();

    private static final String POSTS_FILE_PATH = "posts.json";
    private static final String POSTS_FILE_NAME = "posts.json";

    public static List<Post> readPosts() {
        try {
            File file = new File(POSTS_FILE_NAME);
            if (!file.exists())
                return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Post>>() {}.getType();
            List<Post> posts = gson.fromJson(reader, listType);
            reader.close();
            return posts != null ? posts : new ArrayList<>();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return new ArrayList<>();
        }
    }

    public static void savePost(Post post) {
        List<Post> posts = readPosts();
        posts.add(post);
        try (Writer writer = new FileWriter(POSTS_FILE_PATH)) {
            gson.toJson(posts, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static void removePost(Post post) {
        List<Post> posts = readPosts();

        posts.removeIf(u -> u.getId() == (post.getId()));

        try (Writer writer = new FileWriter(POSTS_FILE_PATH)) {
            gson.toJson(posts, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static int getNextPostId() {
        List<Post> posts = readPosts();
        return ((posts.isEmpty() ? 0 : posts.size()) + 1);
    }

    public static List<Post> getPostbyUserId(int userid) {
        List<Post> userposts = new ArrayList<>();
        for (Post post : readPosts())
            if (post.getUser().getId() == userid)
                userposts.add(post);
        return userposts;
    }

    public static Post getPostById(int id) {
        try {
            List<Post> posts = readPosts();
            for (Post post : posts)
                if (post.getId() == id)
                    return post;
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static void updatePost(Post post) {
        List<Post> posts = readPosts();
        for (int i = 0; i < posts.size(); i++)
            if (posts.get(i).getId() == post.getId()) {
                posts.set(i, post);
                break;
            }
        try (Writer writer = new FileWriter(POSTS_FILE_PATH)) {
            gson.toJson(posts, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

}