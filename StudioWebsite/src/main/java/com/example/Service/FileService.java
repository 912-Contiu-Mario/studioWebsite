package com.example.Service;

import com.example.Model.Album;
import com.example.Model.Content;
import com.example.Model.Image;
import com.example.Model.Video;
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
    public Image saveImage(String albumName, String filename, String path, float image_size)
    {

        Album album = albumRepository.findAlbumByTitle(albumName);
        Image imageToSave = new Image(album.getId(), filename, path, SecurityContextHolder.getContext().getAuthentication().getName(), image_size) ;
        Image savedImage = imageRepository.save(imageToSave);
        imageRepository.flush();
        return savedImage;
    }


    public void deleteImage(int imageId)
    {
        imageRepository.deleteById(imageId);
    }

    public void deleteVideo(int videoId)
    {
        videoRepository.deleteById(videoId);
    }
    public Video saveVideo(String albumName, String filename, String path, float video_size)
    {
        Album album = albumRepository.findAlbumByTitle(albumName);
        Video videoToSave = new Video(album.getId(), filename, path,SecurityContextHolder.getContext().getAuthentication().getName(), video_size);
        Video savedVideo = videoRepository.save(videoToSave);
        videoRepository.flush();
        return savedVideo;
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
            throw new Exception("Content not found");
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

    public Album saveAlbum(Album album)
    {
       Album savedAlbum = albumRepository.save(album);
        albumRepository.flush();
        return savedAlbum;
    }

    public String createThumbnail(File fileToThumbnail, int width, int height) throws IOException {

        String thumbnailName = "thumbnail-"+fileToThumbnail.getName();
        String thumbnailPath = "D:\\ServerFiles\\Thumbnails\\" +thumbnailName;
        Path pathToThumbnail = Paths.get(thumbnailPath);
        if(Files.exists(pathToThumbnail))
            return thumbnailPath;
        File thumbnail = new File(thumbnailPath);
        Thumbnails.of(fileToThumbnail).size(width,height).toFile(thumbnail);
        return thumbnailPath;
    }

}
