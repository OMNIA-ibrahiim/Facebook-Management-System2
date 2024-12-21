package com.example.oop.models;

import com.example.oop.interfaces.Notifiable;
import com.example.oop.utils.NotificationManager;
import com.example.oop.utils.RegularUserManager;
import java.util.List;

public class Message implements Notifiable {
    private final RegularUser sender;
    private final List<Integer> receviers;
    private final String content;

    public Message(RegularUser sender,List<Integer>receviers,  String content) {
        this.sender = sender;
        this.receviers = receviers;
        this.content = content;
    }

    public RegularUser getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void createNotification() {
        int id = NotificationManager.getNextNotificationId();
        String message = this.sender.getName() + " Sent you a message!";
        List<Integer> receviers = this.receviers;

        for (Integer recevierId : receviers) {
            Notification notification = new Notification(id, recevierId, message);
            RegularUser receiver = RegularUserManager.getUserById(recevierId);
            if (receiver != null)
                receiver.setHasNewNotification(true);
        }
    }
}