package com.example.oop.models;

import com.example.oop.utils.*;

public class Admin extends User{

    public Admin(String name, String email, String password, String birthdate, String gender, String phone, int id) {
        super(name, email, password, birthdate, gender, phone, id);
    }

    public void banUser(RegularUser user,String reason){
        user.setBanned(true);
        user.setBanReason(reason);
        user.setBanTimestamp();
    }

    public void unbanUser(RegularUser user){
        user.setBanned(false);
        user.setBanReason("NOTHING");
    }

    public void removeUser(RegularUser user){
        AdminManager.removeUser(user);
    }

    public void removePost(Post post){
        PostManager.removePost(post);
    }
}