package com.example.Service;

import com.example.Model.PasswordException;
import com.example.Model.User;
import com.example.Model.UsernameException;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User saveUser(User user)
    {
        User newUser = userRepository.save(user);
        userRepository.flush();
        return newUser;
    }


    public void validateUsername(String username) throws UsernameException {
        Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]{3,30}$");
        Matcher usernameValidator = usernamePattern.matcher(username);
        boolean validity = usernameValidator.find();
        if(!validity)
            throw new UsernameException("Invalid username(only use letters and digits)");
        if( userRepository.findUserByUsername(username) != null)
            throw new UsernameException("Username taken");
    }
    public void validatePassword(String password) throws PasswordException
    {
        Pattern passwordPattern = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}");
        Matcher passwordValidator = passwordPattern.matcher(password);
        boolean validity = passwordValidator.find();
        if(!validity)
            throw new PasswordException("Invalid password. See rules above");
    }

}
