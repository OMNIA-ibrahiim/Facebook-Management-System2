package com.example.oop.models;

import com.example.oop.utils.NotificationManager;

public class Notification {
    private int id;
    private int userId;
    private String message;

    public Notification(int id,int userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        NotificationManager.saveNotification(this);
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

}
