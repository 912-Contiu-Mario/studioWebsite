package com.example.Controller;
import com.example.Model.PasswordException;
import com.example.Model.User;
import com.example.Model.UsernameException;
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

@CrossOrigin
@Controller
@RequestMapping("")
public class HomeController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public HomeController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "adminPanel")
    public String adminPanel()
    {
        return "adminPanel";
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