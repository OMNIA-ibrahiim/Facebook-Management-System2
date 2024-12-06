package com.example.oop.models;

import com.sun.javafx.scene.shape.ArcHelper;

import java.util.ArrayList;

public class RegularUser extends User{

    private ArrayList<RegularUser> friends;
    private ArrayList<RegularUser> restrictedFriends;
    private ArrayList<Post> posts;
    private boolean banned;

    public RegularUser(String name, String email, String password, String birthdate, String gender, String phone, String id) {
        super(name, email, password, birthdate, gender, phone, id);
        friends = new ArrayList<>();
        restrictedFriends = new ArrayList<>();
        posts = new ArrayList<>();
        banned = false;
    }

    public ArrayList<RegularUser> getFriends() {
        return friends;
    }

    public ArrayList<RegularUser> getRestrictedFriends() {
        return restrictedFriends;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void addFriend(RegularUser user) {
        boolean isAlreadyFriend=false;
        ArrayList<RegularUser> friends = this.getFriends();
        ArrayList<RegularUser> restrictedfriends = this.getRestrictedFriends();

        if(friends.contains(user)||restrictedfriends.contains(user)){
            isAlreadyFriend = true;
        }
        if(!isAlreadyFriend)
            friends.add(user);
    }

    public void addRestrictedFriend(RegularUser user) {
        restrictedFriends.add(user);
    }

    public void removeFriend(RegularUser user) {
        friends.remove(user);
    }

    public void removeRestrictedFriend(RegularUser user) {
        restrictedFriends.remove(user);
    }

    public void tagUser(RegularUser user,Post post){
        /*if (!post.getTaggedUsers().contains(user)) {
            post.addTaggedUser(user);
            System.out.println(user.getName() + " has been tagged in the post.");
        } else {
            System.out.println(user.getName() + " is already tagged in the post.");
        }*/
    }

    public void likePost(Post post){
        /*if (!post.getLikes().contains(this)) {
            post.addLike(this);
        }*/
    }

    public void unlikePost(Post post){
        /*if (post.getLikes().contains(this)) {
            post.removeLike(this);
        }*/
    }

    public void comment(Post post, String commentText){
        /*Comment comment = new Comment(this, commentText);
        post.addComment(comment);*/
    }

    public void sendMessage(Post post, String commentText){
        /*Message message = new Message(this, receiver, messageContent);
        receiver.receiveMessage(message); */
    }

    public void sharePost(){
        /*Post sharedPost = new Post(post);
        this.posts.add(sharedPost);*/
    }

    public ArrayList<Post> friendship(RegularUser user) {
        ArrayList<Post> mutualPosts = new ArrayList<>();

        // Iterate through the current user's posts
        for (Post post : this.posts) {
            // Check if the other user has shared the same post
            if (user.getPosts().contains(post)) {
                mutualPosts.add(post);
            }
        }

        return mutualPosts;
    }


    public ArrayList<RegularUser> mutualFriends(RegularUser user) {
        ArrayList<RegularUser> friends = new ArrayList<>();

        // Get the friends of both users
        ArrayList<RegularUser> user1Friends = this.getFriends();
        ArrayList<RegularUser> user2Friends = user.getFriends();

        ArrayList<RegularUser> user1RestrictedFriends = this.getRestrictedFriends();
        ArrayList<RegularUser> user2RestrictedFriends = user.getRestrictedFriends();

        // Find mutual friends
        for (RegularUser friend : user1Friends) {
            if (user2Friends.contains(friend) || user1RestrictedFriends.contains(friend)) {
                friends.add(friend);
            }
        }
        for (RegularUser friend : user1RestrictedFriends) {
            if (user2Friends.contains(friend) || user1RestrictedFriends.contains(friend)) {
                friends.add(friend);
            }
        }

        return friends;
    }

    public void displayInfo(){
        System.out.println("User Info:");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Birthdate: " + getBirthdate());
        System.out.println("Gender: " + getGender());
        System.out.println("Phone: " + getPhone());
        System.out.println("ID: " + getId());
    }
}
