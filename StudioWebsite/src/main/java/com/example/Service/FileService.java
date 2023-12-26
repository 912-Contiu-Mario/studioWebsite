package com.example.Service;

import com.example.Model.Album;
import com.example.Model.Content;
import com.example.Model.Image;
import com.example.Model.Video;
import com.example.Repository.AlbumRepository;
import com.example.Repository.ImageRepository;
import com.example.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    VideoRepository videoRepository;



    @Autowired
    AlbumRepository albumRepository;
    public Image saveImage(String albumName, String filename, String path)
    {
        Album album = albumRepository.findAlbumByTitle(albumName);
        Image imageToSave = new Image(album.getId(), filename, path);
        Image savedImage = imageRepository.save(imageToSave);
        imageRepository.flush();
        return savedImage;
    }

    public Video saveVideo(String albumName, String filename, String path)
    {
        Album album = albumRepository.findAlbumByTitle(albumName);
        Video videoToSave = new Video(album.getId(), filename, path);
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


    public Content getContentByName(String fileName) throws Exception
    {
        Content contentToRetrieve = imageRepository.findImageByImageName(fileName);
        if(contentToRetrieve == null)
            contentToRetrieve = videoRepository.findVideoByVideoName(fileName);
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

}
