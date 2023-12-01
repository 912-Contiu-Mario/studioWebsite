package com.example.Service;

import com.example.Model.Image;
import com.example.Model.Video;
import com.example.Repository.ImageRepository;
import com.example.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    VideoRepository videoRepository;
    public Image saveImage(Image image)
    {
        Image savedImage = imageRepository.save(image);
        imageRepository.flush();
        return savedImage;
    }

    public Video saveVideo(Video video)
    {
        Video savedVideo = videoRepository.save(video);
        imageRepository.flush();
        return savedVideo;
    }

}
