package com.example.oop.utils;

import com.example.oop.models.*;
import com.example.oop.models.RegularUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String FILE_PATH = "users.json";
    private static final Gson gson = new Gson();
    private static final String FILE_NAME = "users.json"; // JSON file for storing user data

    public static List<RegularUser> readUsers() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                // If the file doesn't exist, return an empty list
                return new ArrayList<>();
            }

            // Use Gson to read the file content
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<RegularUser>>() {}.getType();
            List<RegularUser> users = gson.fromJson(reader, listType);
            reader.close();

            // Ensure the returned list is not null
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an error
        }
    }

    public static void saveUser(RegularUser user) {
        List<RegularUser> users = readUsers();
        users.add(user);
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to get a user by email and password for login
    public static RegularUser getUserByEmailAndPassword(String email, String password) {
        // Get the list of users from the JSON file
        List<RegularUser> users = readUsers();
        // Iterate through the list of users
        for (RegularUser user : users) {
            // Check if the email and password match
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user; // Return the matching user
            }
        }
        return null; // Return null if no user matches
    }
    public static boolean isDuplicateEmail(String email) {
        return readUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public static boolean authenticateUser(String email, String password) {
        return readUsers().stream().anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static void removeUser(RegularUser user){
        List<RegularUser> users = readUsers();

        // Remove the user from the list
        users.removeIf(u -> u.getEmail().equals(user.getEmail())); // Assumes email is unique

        // Write the updated list back to the file
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }
}
