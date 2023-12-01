package com.example.Service;

import com.example.Model.PasswordException;
import com.example.Model.User;
import com.example.Model.UsernameException;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public void validateUsername(String username) throws UsernameException {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{3,30}$");
        Matcher matcher = pattern.matcher(username);
        boolean validity = matcher.find();
        if(!validity)
            throw new UsernameException("Invalid username(only use letters and digits");
        if( userRepository.findUserByUsername(username) != null)
            throw new UsernameException("Username taken");
    }
    public void validatePassword(String password) throws PasswordException
    {
        if(password.length()<8)
            throw new PasswordException("Invalid password. See rules above");
        Pattern pattern = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}");
        Matcher matcher = pattern.matcher(password);
        boolean validity = matcher.find();
        if(!validity)
            throw new PasswordException("Invalid password. See rules above");
    }

}
