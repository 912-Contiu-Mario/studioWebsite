package com.example.Service;

import com.example.Model.*;
import com.example.Repository.AlbumRepository;
import com.example.Repository.ImageRepository;
import com.example.Repository.VideoRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.query.PartTreeJpaQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class FileService {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    AlbumRepository albumRepository;

    public Image saveImage(MultipartFile image, String albumName) throws FileException, RepositoryException {
        String filename = image.getOriginalFilename();
        String filepath = "D:/ServerFiles/" + albumName +"/" + filename;
        try{
            image.transferTo(new File(filepath));}
        catch(IOException exception){
            throw new FileException("Image couldn't be saved on disk");
        }
        double bytesSize = image.getSize();
        float megabytesSize = (float) bytesSize/(1000*1000);
        int aux =(int) (megabytesSize * 100);
        megabytesSize = (float)aux/100;

        try {
            Album album = albumRepository.findAlbumByTitle(albumName);
            Image imageToSave = new Image(album.getId(), filename, filepath, SecurityContextHolder.getContext().getAuthentication().getName(), megabytesSize);
            Image savedImage = imageRepository.save(imageToSave);
            imageRepository.flush();
            return savedImage;
        }
        catch(Exception exception)
        {
            throw new RepositoryException("Image couldn't be saved in database");
        }
    }


    public String fileType(MultipartFile file) throws FileException {
        String filename = file.getOriginalFilename();
        if (filename != null && (filename.endsWith(".mp4") || filename.endsWith(".avi") || filename.endsWith(".mov")))
            return "video";
        else if (filename != null && (filename.endsWith(".jpeg")||filename.endsWith(".jpg") || filename.endsWith(".png")))
            return "image";
        else throw new FileException("File type unsupported");
    }

    public void deleteContent(Content contentToDelete, String albumToDeleteFrom) throws FileException, RepositoryException {
        String fileName =contentToDelete.getFileName();
        String filepath = "D:/ServerFiles/" + albumToDeleteFrom +"/" + fileName;
        Path filePath = Paths.get(filepath);
        try {
             Files.deleteIfExists(filePath);
        }
        catch(IOException exception){
            throw new FileException("Couldn't delete file from disk");
        }
        try{
            if(contentToDelete instanceof Image)
                imageRepository.deleteById(contentToDelete.getFileId());
            else if(contentToDelete instanceof Video)
                videoRepository.deleteById(contentToDelete.getFileId());}
        catch(Exception exception) {
            throw new RepositoryException("Couldn't delete file from database");
        }
    }

    public void deleteVideo(int videoId)
    {
        videoRepository.deleteById(videoId);
    }

    public Video saveVideo(MultipartFile video, String albumName) throws FileException, RepositoryException {
        String filename = video.getOriginalFilename();
        String filepath = "D:/ServerFiles/" + albumName +"/" + filename;
        try{
        video.transferTo(new File(filepath));}
        catch(IOException exception){
            throw new FileException("Couldn't delete video from disk");
        }
        double bytesSize = video.getSize();
        float megabytesSize = (float) bytesSize/(1000*1000);
        int aux =(int) (megabytesSize * 100);
        megabytesSize = (float)aux/100;
        try{
            Album album = albumRepository.findAlbumByTitle(albumName);
            Video videoToSave = new Video(album.getId(), filename, filepath,SecurityContextHolder.getContext().getAuthentication().getName(), megabytesSize);
            Video savedVideo = videoRepository.save(videoToSave);
            videoRepository.flush();
            return savedVideo;}
        catch (Exception exception){
            throw  new RepositoryException("Coudln't delete video from database");
        }
    }

    public List<Album> getAlbums()
    {
        List<Album> albums=albumRepository.findAll();
       return albums;
    }

    public List<Image> getImages()
    {
        List<Image> images = imageRepository.findAll();
        return images;
    }


    public Content getContentById(int fileId) throws Exception
    {
        Content contentToRetrieve = imageRepository.findImageById(fileId);
        if(contentToRetrieve == null)
            contentToRetrieve = videoRepository.findVideoById(fileId);
        if(contentToRetrieve == null)
            throw new FileException("Content not found");
        return contentToRetrieve;
    }

    public List<Content> getContentFromAlbum(String albumName) throws Exception
    {
        Album album = albumRepository.findAlbumByTitle(albumName);
        if(album == null)
        {
            throw new Exception("Album with that name doesn't exist");
        }
        List<Image> imageList = imageRepository.findImagesByAlbumId(album.getId());
        List<Video> videoList = videoRepository.findVideosByAlbumId(album.getId());
        List<Content> contentList = new ArrayList<Content>();
        contentList.addAll(imageList);
        contentList.addAll(videoList);
        return  contentList;
    }

    public Album saveAlbum(Album album) throws RepositoryException, FileException {
        String folderPath = "D:\\ServerFiles\\" + album.getAlbum_title();
        Path newFolderPath = Paths.get(folderPath);
        try{
        Files.createDirectory(newFolderPath);}
        catch(IOException exception){
            throw new FileException("Couldn't save album on disk");
        }
        try{
       Album savedAlbum = albumRepository.save(album);
        albumRepository.flush();
        return savedAlbum;}
        catch(Exception exception)
        {
            throw new RepositoryException("Couldn't save album in database");
        }
    }

    public String createThumbnail(File fileToThumbnail, int width, int height) throws FileException {

        String thumbnailName = "thumbnail-"+fileToThumbnail.getName();
        String thumbnailPath = "D:\\ServerFiles\\Thumbnails\\" +thumbnailName;
        Path pathToThumbnail = Paths.get(thumbnailPath);
        if(Files.exists(pathToThumbnail))
            return thumbnailPath;
        File thumbnail = new File(thumbnailPath);
        try{
        Thumbnails.of(fileToThumbnail).size(width,height).toFile(thumbnail);
        return thumbnailPath;}
        catch(IOException exception)
        {
            throw new FileException("Couldn't save thumbnail on disk");
        }
    }

}
