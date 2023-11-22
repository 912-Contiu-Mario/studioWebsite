package com.example.Service;

import com.example.Model.User;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User getUserByUsername(String username)
    {
        return userRepository.findUserByUsername(username);
    }

    public User createUser(User user)
    {
        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;

    }

}
