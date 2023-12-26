package com.example.Controller;
import com.example.Model.*;
import com.example.Service.FileService;
import com.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

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


    @RequestMapping(value = "adminPanel",method = RequestMethod.GET, params = "getAlbums")
    public ResponseEntity<List<Album>> retrieveAlbums() {
       return ResponseEntity.ok(fileService.getAlbums());
    }
//    @RequestMapping(value = "/adminPanel/album", method =  RequestMethod.GET)
//    public String albumPage()
//
//    {
//        return "albumView";
//    }
    @GetMapping(value = "/adminPanel/content", params = "id")
    public ResponseEntity<Resource> serveContent(@RequestParam String id)
    {

        try{
            Content contentToRetrieve = fileService.getContentByName(id);
            Resource resource = new FileSystemResource(contentToRetrieve.getPath());
            String filename = resource.getFilename();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Set the appropriate content type
                    .body(resource);

        }
        catch(Exception exception)
        {
            return ResponseEntity.badRequest().build();
        }



    }
    @RequestMapping(value = "/adminPanel/album", method = RequestMethod.GET, params = "id")
    public ResponseEntity<List<Content>> retrieveContentFromAlbum(@RequestParam String id)
    {

        try{
             return ResponseEntity.ok(fileService.getContentFromAlbum(id));
        }
        catch (Exception exception)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }


    //needs better error handling
    @RequestMapping(value = "/adminPanel", method = RequestMethod.POST, params = "createAlbum")
    public String createAlbum(@RequestBody Album album)
    {
        try{
            String folderPath = "D:\\ServerFiles\\" + album.getAlbum_title();
            Path newFolderPath = Paths.get(folderPath);
            Files.createDirectory(newFolderPath);
            Album savedAlbum = fileService.saveAlbum(album);
            if(savedAlbum == null)
                return "redirect:/register?error";
            return "redirect:/register";
        }
        catch (Exception exception)
        {
            return "redirect:/register?error";
        }


    }
    @RequestMapping(value = "/adminPanel", method = RequestMethod.POST, params = "upload")
    public  ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("albumName")String albumName)
    {
        try {

            String filename = file.getOriginalFilename();
            String filepath = "D:/ServerFiles/" + albumName +"/" + filename;
            file.transferTo(new File(filepath));
            if (filename != null && (filename.endsWith(".mp4") || filename.endsWith(".avi") || filename.endsWith(".mov"))) {
                Video savedVideo = fileService.saveVideo(albumName, filename, filepath);
            } else if (filename != null && (filename.endsWith(".jpg") || filename.endsWith(".png"))) {
                Image savedImage = fileService.saveImage(albumName, filename, filepath);
            }
            else throw new Exception("File type not supported");
            return ResponseEntity.ok("File uploaded successfully!");
        }
        catch(Exception exception)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during file upload. Please try again.");
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