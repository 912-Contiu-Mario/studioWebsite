package com.example.ferentiwebsite.Service;
import com.example.ferentiwebsite.Model.User;
import com.example.ferentiwebsite.Repository.userRepo;

import java.util.ArrayList;

public class UserService {
    private userRepo uRepo;
    UserService(userRepo ur){
        uRepo = ur;
    }


}
