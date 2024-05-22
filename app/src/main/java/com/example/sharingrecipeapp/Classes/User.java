package com.example.sharingrecipeapp.Classes;

public class User {
    String name;
    String avt;

    public User() {
    }

    public User(String name, String avt) {
        this.name = name;
        this.avt = avt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
