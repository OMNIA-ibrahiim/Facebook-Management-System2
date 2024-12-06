package com.example.oop.models;

public class RegisteredUser extends User {

    public RegisteredUser(String name, String email, String password, String birthdate, String gender, String phone, String id) {
        super(name, email, password, birthdate, gender, phone, id);
    }

    @Override
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