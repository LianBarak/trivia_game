package com.example.trivia_game;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String profileImage;
    public User()
    {
        this.userId = "trivia";
        this.username = "not installed";
        this.password = " ";
        this.email = " ";
        this.profileImage = " ";
    }
    public User(String username, String password, String email, String profileImage)
    {
        this.userId = " ";
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User(String userId, String username, String password, String email, String profileImage)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


}


