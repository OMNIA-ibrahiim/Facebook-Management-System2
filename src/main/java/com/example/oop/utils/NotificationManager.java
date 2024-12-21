package com.example.oop.utils;

import com.example.oop.models.Notification;
import com.example.oop.models.RegularUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.control.Alert;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static final Gson gson = new Gson();

    private static final String NOTIFICATIONS_FILE_PATH = "notifications.json";
    private static final String NOTIFICATIONS_FILE_NAME = "notifications.json";

    public static List<Notification> readNotifications() {
        try {
            File file = new File(NOTIFICATIONS_FILE_NAME);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Notification>>() {}.getType();
            List<Notification> notifications = gson.fromJson(reader, listType);
            reader.close();

            return notifications != null ? notifications : new ArrayList<>();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return new ArrayList<>();
        }
    }

    public static void saveNotification(Notification notification) {
        List<Notification> notifications = readNotifications();
        notifications.add(notification);
        try (Writer writer = new FileWriter(NOTIFICATIONS_FILE_PATH)) {
            gson.toJson(notifications, writer);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public static int getNextNotificationId() {
        List<Notification> notifications = readNotifications();

        if (notifications.isEmpty())
            return 0;

        int lastNotificationId = 0;
        for (Notification notification : notifications) {
            int notificationId = notification.getId();
            if (notificationId > lastNotificationId)
                lastNotificationId = notificationId;
        }

        return (lastNotificationId + 1);
    }

    public static List<Notification> userNotifications(RegularUser user) {
        try {
            List<Notification> notifications = readNotifications();
            List<Notification> userNotifications = new ArrayList<>();
            for (Notification notification : notifications) {
                if (notification.getUserId() == user.getId())
                    userNotifications.add(notification);
            }
            return userNotifications;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

}
