package com.example.ferentiwebsite.Controller;

import com.example.ferentiwebsite.Model.User;
import com.example.ferentiwebsite.Service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService service;

    private JdbcTemplate query = new JdbcTemplate();

//    @PostMapping
//    public void addUser(@RequestBody User user){
//        String command = "INSERT INTO "
//
//    }
    @GetMapping
    public
}
