package com.example.sporteam.Model;

import java.util.Random;
import java.util.UUID;

public class User {
    private String phone;
    private String name;
    private String password;
    private String date;
    private String gender;
    private String id;

    public User() {
    }

    public User(String phone,String name, String password,String date,String gender) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.date=date;
        this.gender=gender;
        this.id= UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
