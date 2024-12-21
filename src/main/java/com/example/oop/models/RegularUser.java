package com.example.oop.models;

import com.example.oop.utils.PostManager;
import com.example.oop.utils.RegularUserManager;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegularUser extends User {

    private ArrayList<Integer> friends;
    private ArrayList<Integer> restrictedFriends;
    private boolean banned;
    private String banTimestamp;
    private String banReason;
    private boolean hasNewNotification;

    public RegularUser(String name, String email, String password, String birthdate, String gender, String phone, Integer id) {
        super(name, email, password, birthdate, gender, phone, id);
        friends = new ArrayList<>();
        restrictedFriends = new ArrayList<>();
        banned = false;
        banReason = "NOTHING";
        banTimestamp = "00:00:00";
        hasNewNotification = false;
        RegularUserManager.saveUser(this);
    }

    public ArrayList<Integer> getFriends() {
        return friends;
    }

    public ArrayList<Integer> getRestrictedFriends() {
        return restrictedFriends;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
        RegularUserManager.updateUser(this);
    }

    public void setBanTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.banTimestamp = LocalDateTime.now().format(formatter);
        RegularUserManager.updateUser(this);
    }

    public String getFriendship(RegularUser user) {
        try {
            if (this.getFriends() == null && this.getRestrictedFriends() == null)
                    return "notFriend";
            if (this.getFriends().contains(user.getId()))
                return "normal";
            else if (this.getRestrictedFriends().contains(user.getId()))
                return "restricted";
            else
                return "notFriend";
        }catch (NullPointerException e){
            return "notFriend";
        }
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
        RegularUserManager.updateUser(this);
    }

    public String getBanTimestamp() {
        return banTimestamp;
    }

    public String getBanReason() {
        return banReason;
    }

    public boolean HasNewNotification() {
        return hasNewNotification;
    }

    public void setHasNewNotification(boolean hasNewNotification) {
        this.hasNewNotification = hasNewNotification;
        RegularUserManager.updateUser(this);
    }

    public void addFriend(RegularUser user, String type) {
        try {
            friends.remove(user.getId());
            user.friends.remove(this.getId());
            restrictedFriends.remove(user.getId());
            user.restrictedFriends.remove(this.getId());
            if (type.equals("Normal Friend")) {
                friends.add(user.getId());
                user.friends.add(this.getId());
            }else if (type.equals("Restricted Friend")) {
                restrictedFriends.add(user.getId());
                user.restrictedFriends.add(this.getId());
            }
            RegularUserManager.updateUser(this);
            RegularUserManager.updateUser(user);
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
        }
    }

    public void removeFriend(RegularUser user) {
        friends.remove(user.getId());
        restrictedFriends.remove(user.getId());
        user.friends.remove(this.getId());
        user.restrictedFriends.remove(this.getId());

        RegularUserManager.updateUser(this);
        RegularUserManager.updateUser(user);
    }

    public ArrayList<Post> mutualPosts(RegularUser user) {
        try {
            List<Post> allPosts = PostManager.readPosts();
            ArrayList<Post> mutualPosts = new ArrayList<>();
            ArrayList<Post> user1Posts = new ArrayList<>();
            ArrayList<Post> user2Posts = new ArrayList<>();
            for (Post post : allPosts) {
                if (post.getUser() != null && Objects.equals(post.getUser().getId(), this.getId()))
                    user1Posts.add(post);
                if (post.getUser() != null && Objects.equals(post.getUser().getId(), user.getId()))
                    user2Posts.add(post);
            }
            for (Post post1 : user1Posts)
                for (Post post2 : user2Posts) {
                    if (post1.getContent().equals(post2.getContent())) {
                        String privacy = post2.getPrivacy();
                        String relation = this.getFriendship(user);
                        if ((relation.equals("restricted") || relation.equals("notFriend")) && privacy.equals("Private"))
                            continue;
                        mutualPosts.add(post1);
                    }
                }
            return mutualPosts;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error Happened").show();
            return new ArrayList<>();
        }
    }

    public ArrayList<Integer> mutualFriends(RegularUser user) {
        try {
            ArrayList<Integer> friends = new ArrayList<>();
            ArrayList<Integer> user1Friends = this.getFriends();
            ArrayList<Integer> user2Friends = user.getFriends();

            ArrayList<Integer> user1RestrictedFriends = this.getRestrictedFriends();
            ArrayList<Integer> user2RestrictedFriends = user.getRestrictedFriends();

            for (Integer friend : user1Friends)
                if (user2Friends.contains(friend) || user2RestrictedFriends.contains(friend))
                    friends.add(friend);
            for (Integer friend : user1RestrictedFriends)
                if (user2Friends.contains(friend) || user2RestrictedFriends.contains(friend))
                    friends.add(friend);

            return friends;
        }catch (NullPointerException e){
            return new ArrayList<>();
        }
    }

}