package com.example.Model;

import java.util.UUID;

public class User {
    private final String username;
    private final String pass;

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

    @Override
    public boolean equals(Object userToCompare) {
        if (userToCompare instanceof User user)
        {
            return this.username.equals(user.getUsername()) && this.pass.equals(user.getPass());
        }
        return false;
    }
}
