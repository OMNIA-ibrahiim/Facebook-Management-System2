package com.example.oop.models;

import com.example.oop.utils.*;

public class Admin extends User{

    public Admin(String name, String email, String password, String birthdate, String gender, String phone, String id) {
        super(name, email, password, birthdate, gender, phone, id);
    }

    public void banUser(RegularUser user){
        user.setBanned(true);
    }

    public void unbanUser(RegularUser user){
        user.setBanned(false);
    }

    public void removeUser(RegularUser user){
        FileManager.removeUser(user);
    }

    public void displayInfo() {
        System.out.println("User Info:");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Birthdate: " + getBirthdate());
        System.out.println("Gender: " + getGender());
        System.out.println("Phone: " + getPhone());
        System.out.println("ID: " + getId());
    }

}
