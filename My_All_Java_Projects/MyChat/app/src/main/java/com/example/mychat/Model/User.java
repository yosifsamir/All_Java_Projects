package com.example.mychat.Model;

public class User {
    private String userName;
    private String email;
    private String password;
    private String imagePath;

    public User(String userName, String email, String password, String imagePath) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
    }


    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
