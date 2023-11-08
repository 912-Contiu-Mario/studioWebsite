package com.example.Service;
import com.example.Model.User;
import com.example.Repository.userRepo;

import java.util.ArrayList;

public class UserService {
    private userRepo uRepo;
    UserService(userRepo ur){
        uRepo = ur;
    }

}
