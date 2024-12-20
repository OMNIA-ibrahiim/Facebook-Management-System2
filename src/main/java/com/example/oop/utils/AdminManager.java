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

public class AdminManager {

    private static final Gson gson = new Gson();

    private static final String ADMINS_FILE_PATH = "ADMINS.json";
    private static final String ADMINS_FILE_NAME = "ADMINS.json";

    public static List<Admin> readAdmins() {
        try {
            File file = new File(ADMINS_FILE_NAME);
            if (!file.exists())
                return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Admin>>() {}.getType();
            List<Admin> admins = gson.fromJson(reader, listType);
            reader.close();

            return admins != null ? admins : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAdmin(Admin admin) {
        List<Admin> admins = readAdmins();
        admins.add(admin);
        try (Writer writer = new FileWriter(ADMINS_FILE_PATH)) {
            gson.toJson(admins, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static Admin getAdminByEmailAndPassword(String email, String password) {
        List<Admin> admins = readAdmins();
        for (Admin admin : admins)
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password))
                return admin;
        return null;
    }

    public static void removeUser(RegularUser user){
        List<Post> posts = PostManager.readPosts();
        if(!posts.isEmpty())
            for(Post post : posts)
                if(post.getUser().getId() == user.getId())
                    PostManager.removePost(post);
        RegularUserManager.removeUser(user);
    }

    public static int returnNextAdminId() {
        List<Admin> admins = readAdmins();

        if (admins.isEmpty())
            return 0;

        int lastAdminId = 0;
        for (Admin admin : admins) {
            int adminId = admin.getId();
            if (adminId > lastAdminId) {
                lastAdminId = adminId;
            }
        }

        return (lastAdminId + 1);
    }

    public static boolean validateAdminPassword(String pass){
        return pass.equals("0000");
    }

    public static boolean isDuplicateEmail(String email) {
        return readAdmins().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    public static List<RegularUser> BannedUsers() {
        try {
            List<RegularUser> users = RegularUserManager.readUsers();

            if (users == null || users.isEmpty())
                return new ArrayList<>();

            List<RegularUser> bannedUsers = new ArrayList<>();
            for (RegularUser user : users)
                if (user.isBanned())
                    bannedUsers.add(user);

            return bannedUsers;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
        return new ArrayList<>();
    }


}
