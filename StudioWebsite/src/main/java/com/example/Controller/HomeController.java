package com.example.Controller;
import com.example.Model.*;
import com.example.Service.FileService;
import com.example.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@CrossOrigin
@Controller
@RequestMapping("")
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public HomeController(UserService userService, FileService fileService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.fileService = fileService;
    }

    @RequestMapping(value = "adminPanel")
    public String adminPanel()
    {
        return "adminPanel";
    }

    @RequestMapping(value = "/adminPanel", method = RequestMethod.POST, params = "upload")
    public void uploadFile(@RequestParam("file")MultipartFile file)
    {
        try {

            String filename = file.getOriginalFilename();
            String filepath = "D:/ServerFiles/" + filename;
            file.transferTo(new File(filepath));
            if (filename != null && (filename.endsWith(".mp4") || filename.endsWith(".avi"))) {
                Video videoToSave = new Video(1,filename, filepath);
                Video savedVideo = fileService.saveVideo(videoToSave);
            } else if (filename != null && (filename.endsWith(".jpg") || filename.endsWith(".png"))) {
                Image imageToSave = new Image(1,filename, filepath);
                Image savedImage = fileService.saveImage(imageToSave);
            }


        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }

    }

    @RequestMapping(value = "")
    public String index(){return "index";}

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestBody User user)
    {
        try{
            String password = user.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
            User newUser = userService.saveUser(user);
            if(newUser == null){
                return "redirect:/register?error";
            }
            return "redirect:/register?success";
        }
        catch (Exception e){
            return "redirect:/register?error";
        }
    }


}