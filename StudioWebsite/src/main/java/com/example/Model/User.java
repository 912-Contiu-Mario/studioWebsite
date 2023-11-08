package com.example.Model;

public class User {
    private String username;
    private String pass;

    public User(String username, String password) {
        this.username = username;
        this.pass = password;
    }

    public String getUsername(){
        return username;
    }
    public String getPass()
    {
        return pass;
    }

    @Override
    public String toString() {
        return "Username: " + username +
                "\nPassword: " + pass;
    }
}
