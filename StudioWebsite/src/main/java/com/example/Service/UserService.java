package com.example.Service;

import com.example.Model.User;
import com.example.Repository.userRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final userRepo uRepo;
    UserService(userRepo ur){
        uRepo = ur;
    }

    public Boolean verifyLogin(User userToVerify){
        for (User user : uRepo.getUsers())
        {
            if (userToVerify.equals(user))
                return true;
        }
        return false;
    }
}
