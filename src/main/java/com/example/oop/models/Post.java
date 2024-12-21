package com.example.oop.models;

import com.example.oop.interfaces.Notifiable;
import com.example.oop.utils.NotificationManager;
import com.example.oop.utils.RegularUserManager;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Notifiable {
    private  int id ;
    private final Date timestamp;
    private final RegularUser user;
    private ArrayList<Integer>likes;
    private String content;
    private String privacy;
    private ArrayList<Integer> taggedUsers;

    public Post(String content, RegularUser user,int id,String privacy,ArrayList<Integer> taggedUsers) {
        this.content = content;
        this.user = user;
        this.timestamp = new Date();
        this.id = id;
        this.privacy = privacy;
        this.likes=new ArrayList<>();
        this.taggedUsers = taggedUsers;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public RegularUser getUser() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void addLike(Integer userId) {
        try {
            likes.add(userId);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public void removeLike(Integer userId) {
        try {
            likes.remove(userId);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public ArrayList<Integer> getLikes() {
        return likes != null ? likes : new ArrayList<>();
    }

    public int getLikeCounter() {
        return likes!=null?likes.size():0;
    }

    public ArrayList<Integer> getTaggedUsers() {
        return taggedUsers !=null? taggedUsers :new ArrayList<>();
    }

    @Override
    public void createNotification() {
        int id = NotificationManager.getNextNotificationId();
        String message = this.user.getName() + " Shared a Post!";
        String privacy = this.getPrivacy();
        List<Integer> friends = this.user.getFriends();
        List<Integer> restrictedFriends = this.user.getRestrictedFriends();

        if (privacy.equals("Public"))
            for (Integer friendId : restrictedFriends) {
                Notification notification = new Notification(id, friendId, message);
            }
        for (Integer friendId : friends) {
            Notification notification = new Notification(id, friendId, message);
            RegularUser friend = RegularUserManager.getUserById(friendId);
            if(friend != null)
                friend.setHasNewNotification(true);
        }

        message = this.user.getName() + " Tagged you in a Post!";
        try {
            if (!taggedUsers.isEmpty()) {
                for (Integer taggeduserId : this.taggedUsers) {
                    Notification notification = new Notification(id, taggeduserId, message);
                    RegularUser taggeduser = RegularUserManager.getUserById(taggeduserId);
                    if(taggeduser != null)
                        taggeduser.setHasNewNotification(true);
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error when creating the notification").show();
        }

    }

    public void createNotification(RegularUser notifiedUser, RegularUser senderUser){
        int id = NotificationManager.getNextNotificationId();
        String message = senderUser.getName() + " Liked your Post!";
        Notification notification = new Notification(id, notifiedUser.getId(), message);
        notifiedUser.setHasNewNotification(true);
    }
}