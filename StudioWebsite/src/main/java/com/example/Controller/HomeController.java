package com.example.Controller;
import com.example.Model.*;
import com.example.Service.FileService;
import com.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "adminPanel";
    }


    @GetMapping(value = "/adminPanel/connectedUser")
    public ResponseEntity<Authentication> getConnectedUser(){
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    }



    //fix this shit now
    @RequestMapping(value ="/adminPanel/downloadZipContent", method = RequestMethod.POST)
    public ResponseEntity<FileSystemResource> downloadZippedContent(@RequestBody List<Integer> contentIds){
        try{
            String zipFilePath = fileService.zipContent(contentIds);
            FileSystemResource zipFileResource = new FileSystemResource(zipFilePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(zipFileResource);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().build();
        }
    }


    @RequestMapping(value ="/adminPanel/downloadZipAlbum")
    public ResponseEntity<FileSystemResource> DownloadZippedAlbum(@RequestParam("albumTitle") String albumTitle){

        try{
            String zipFilePath = fileService.zipFolder(albumTitle);
            FileSystemResource zipFileResource = new FileSystemResource(zipFilePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(zipFileResource);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().build();
        }
    }


     @RequestMapping(value = "/adminPanel",method = RequestMethod.GET, params = "getAlbums")
    public ResponseEntity<List<Album>> retrieveAlbums() {
        return ResponseEntity.ok(fileService.getAlbums());
    }

    @GetMapping(value = "/adminPanel/thumbnails")
    public ResponseEntity<Resource> serveThumbnails(@RequestParam int id, @RequestParam int thumbnailWidth,  @RequestParam int thumbnailHeight)
    {
        try{
            Content contentToRetrieve = fileService.getContentById(id);
            Resource resource = new FileSystemResource(contentToRetrieve.getPath());
            //serve thumbnail of file
            File fileToThumbnail = resource.getFile();
            String thumbnailPath = fileService.createThumbnail(fileToThumbnail, thumbnailWidth, thumbnailHeight);
            Resource thumbnailResource = new FileSystemResource(thumbnailPath);
            String fileType = fileService.fileType(id);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(thumbnailResource);

        }
        catch(Exception exception)
        {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping(value = "/adminPanel/content")
    public ResponseEntity<Resource> serveContent(@RequestParam int id)
    {
        try{
            Content contentToRetrieve = fileService.getContentById(id);
            String contentType = fileService.fileType(id);
            Resource resource = new FileSystemResource(contentToRetrieve.getPath());
            if(contentType.equals(".jpeg")||contentType.equals(".jpg")||contentType.equals(".png")){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Set the appropriate content type
                        .body(resource);
            }
            else{
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("video/"+contentType.substring(1)))  // Set the appropriate video content type
                        .body(resource);
            }
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

    @RequestMapping(value = "/adminPanel", method = RequestMethod.POST, params = "createAlbum")
    public String createAlbum(@RequestBody Album album)
    {
        try{
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

        try{
            String fileType = fileService.fileType(file);
            if(fileType.equals("image")){
                Image savedImage = fileService.saveImage(file, albumName);}
            else if(fileType.equals("video")){
                Video savedVideo = fileService.saveVideo(file, albumName);}
            else throw new Exception("File type not supported");
            return ResponseEntity.ok("File uploaded successfully!");
        }
        catch(Exception exception)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during file upload. Please try again.");
        }
    }

    @RequestMapping(value = "/adminPanel", method = RequestMethod.POST, params = "removeContent")
    public  ResponseEntity<?> removeContent(@RequestParam("id") int imageId, @RequestParam("albumId") String albumToDeleteFrom ){
        try{
            Content contentToDelete = fileService.getContentById(imageId);
            fileService.deleteContent(contentToDelete, albumToDeleteFrom);
        }
        catch(Exception exception)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Couldn't remove file. Please try again.");
        }
        return ResponseEntity.ok("File removed successfully");
    }

    @RequestMapping(value = "")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/adminPanel/register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/adminPanel/register", method = RequestMethod.POST)
    public String registerUser(@RequestBody User user)
    {
        try{
            String password = user.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            String encodedPassword = encoder.encode(password);
            user.setPassword(encodedPassword);
            User newUser = userService.saveUser(user);
            if(newUser == null){
                return "redirect:/adminPanel/register?error";
            }
            return "redirect:/adminPanel/register?success";
        }
        catch (Exception e){
            return "redirect:/adminPanel/register?error";
        }
    }
}