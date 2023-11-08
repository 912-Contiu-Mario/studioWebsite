package com.example.Controller;

import com.example.Model.User;
import com.example.Service.UserService;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin

@RequestMapping()
public class UserController {
    //private UserService service;

    @Autowired
    private final JdbcTemplate query = new JdbcTemplate();

    @PostMapping
    public void logIn(@RequestBody User user)
    {
        System.out.println(user);

        String sql = "SELECT * FROM Users WHERE username='" + user.getUsername() + "' AND password='" + user.getPass()+"'";
        try
        {
            Map<String, Object> res = query.queryForMap(sql);
            System.out.println("Login succesfull!");
        }
        catch (Exception e)
        {
            System.out.println("Invalid username or password!");
        }
    }
}
