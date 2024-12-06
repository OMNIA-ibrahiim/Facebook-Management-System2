package com.example.oop.models;

public abstract class User {
    private final String name;
    private final String email;
    private final String password;
    private final String birthdate;
    private final String gender;
    private final String phone;
    private final String id;

    public User(String name, String email, String password, String birthdate, String gender, String phone, String id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phone = phone;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public abstract void displayInfo();
}
