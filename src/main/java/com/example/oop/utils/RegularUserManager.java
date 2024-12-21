package com.example.oop.utils;

import com.example.oop.models.*;
import com.example.oop.models.RegularUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegularUserManager {

    private static final Gson gson = new Gson();

    private static final String USERS_FILE_PATH = "users.json";
    private static final String USERS_FILE_NAME = "users.json";

    public static List<RegularUser> readUsers() {
        try {
            File file = new File(USERS_FILE_NAME);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RegularUser>>() {}.getType();
            List<RegularUser> users = gson.fromJson(reader, listType);
            reader.close();

            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return new ArrayList<>();
        }
    }

    public static void saveUser(RegularUser user) {
        List<RegularUser> users = readUsers();
        users.add(user);
        try (Writer writer = new FileWriter(USERS_FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static RegularUser getUserByEmailAndPassword(String email, String password) {
        List<RegularUser> users = readUsers();
        for (RegularUser user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static RegularUser findUserByName(String name) {
        List<RegularUser> allUsers = new ArrayList<>();
        try {
            allUsers = RegularUserManager.readUsers();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
        for (RegularUser user : allUsers) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static RegularUser getUserById(int id) {
        try {
            List<RegularUser> users = readUsers();
            for (RegularUser user : users)
                if (user.getId() == id)
                    return user;
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static boolean isDuplicateEmail(String email) {
        return readUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public static boolean isDuplicateName(String name) {
        return readUsers().stream().anyMatch(user -> user.getName().equals(name));
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    public static void removeUser(RegularUser user){
        List<RegularUser> users = readUsers();

        users.removeIf(u -> Objects.equals(u.getId(), user.getId()));

        try (Writer writer = new FileWriter(USERS_FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static int returnNextUserId() {
        List<RegularUser> users = readUsers();

        if (users.isEmpty())
            return 0;

        int lastUserId = 0;
        for (User user : users) {
            int userId = user.getId();
            if (userId > lastUserId)
                lastUserId = userId;
        }

        return (lastUserId + 1);
    }

    public static void updateUser(RegularUser updatedUser) {
        List<RegularUser> users = readUsers();

        for (int i = 0; i < users.size(); i++)
            if (Objects.equals(users.get(i).getId(), updatedUser.getId())) {
                users.set(i, updatedUser);
                break;
            }

        try (Writer writer = new FileWriter(USERS_FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }
}