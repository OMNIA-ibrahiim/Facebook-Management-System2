package com.example.oop.models;

import com.example.oop.interfaces.Notifiable;
import com.example.oop.utils.NotificationManager;
import com.example.oop.utils.PostManager;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Comment implements Notifiable {
    private int id;
    private RegularUser user;
    private String content;
    private ArrayList<Integer> likes;
    private ArrayList<Comment> replies;
    private int postId;
    private final Date timestamp;

    public Comment(int id,Post post, RegularUser user, String content) {
        this.id = id;
        this.likes = new ArrayList<>();
        this.timestamp = new Date();
        this.user = user;
        this.postId = post.getId();
        this.content = content;
        this.replies = new ArrayList<>();
    }

    public RegularUser getUser() {
        return user;
    }

    public void setUser(RegularUser user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
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
        return likes!=null?likes:new ArrayList<>();
    }

    public int getLikeCounter() {
        return likes!=null?likes.size():0;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void createNotification() {
        int id = NotificationManager.getNextNotificationId() + 1;
        String message = this.user.getName() + " Commented on your Post!";
        Notification notification = new Notification(id, Objects.requireNonNull(PostManager.getPostById(this.postId)).getUser().getId(), message);
        Objects.requireNonNull(PostManager.getPostById(this.postId)).getUser().setHasNewNotification(true);
    }

    public void createNotification(RegularUser likeUser){
        int id = NotificationManager.getNextNotificationId();
        String message = likeUser.getName() + " Liked your Comment on " + Objects.requireNonNull(PostManager.getPostById(this.postId)).getUser().getName() + " Post";
        Notification notification = new Notification(id, this.user.getId(), message);
        this.user.setHasNewNotification(true);
    }

}
