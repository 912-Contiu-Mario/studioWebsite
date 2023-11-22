package com.example.Controller;

import com.example.Model.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.StringBufferInputStream;
import java.util.UUID;


//@Controller
//public class UserController {
//    private UserService uService;
//
//    private String connectionToken;
//
//    public UserController(UserService service) {
//        this.uService = service;
//    }
//
//    @RequestMapping("/")
//    public String welcome() {
//        String token = UUID.randomUUID().toString();
//
//        return "login";
//    }
//
//    @RequestMapping("/adminPanel")
//    public String adminPanel() {
//        if (connectionToken == null)
//            return "noAccess";
//        else
//        {
//            return "adminPanel";
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<String> logIn(@RequestBody com.example.Model.User userToLogin)
//    {
//        boolean isValidUser = uService.verifyLogin(userToLogin);
//        if (isValidUser) {
//            String uniqueID = UUID.randomUUID().toString();
//            System.out.println(uniqueID);
//            connectionToken = uniqueID;
//            return new ResponseEntity<>(connectionToken, HttpStatus.OK);
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
//        }
//    }
//}
