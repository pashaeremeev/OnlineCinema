package com.example.onlinecinema.entities;

import java.util.UUID;

public class User {

    private int id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.id = UUID.randomUUID().hashCode();
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
