package com.example.Service;

import com.example.Model.*;


import com.example.Repository.AlbumRepository;
import com.example.Repository.ImageRepository;
import com.example.Repository.VideoRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    AlbumRepository albumRepository;


    String serverFilesPath = "D:/ServerFiles/";
    String thumbnailsPath = "D:/ServerFiles/Thumbnails/";
    String zipFilesPath = "D:/ServerFiles/ZipFiles/";


    public String createZipFilePath(String zipFileName)
    {
        return zipFilesPath+zipFileName;
    }
    public String createFilePath(String albumName, String filename){
        return serverFilesPath + albumName +"/" + filename;
    }

    public String createAlbumPath(String albumName){
        return serverFilesPath+albumName;
    }

    public String createThumbnailPath(String thumbnailName){
        return thumbnailsPath+thumbnailName;
    }

    public Image saveImage(MultipartFile image, String albumName) throws FileException, RepositoryException {
        String filename = image.getOriginalFilename();
        String filepath = createFilePath(albumName, filename);
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
            album.setAlbum_size(album.getAlbum_size() + imageToSave.getImage_size());
            albumRepository.save(album);
            albumRepository.flush();
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

    public String fileType(String filename) throws FileException {
        if (filename != null && (filename.endsWith(".mp4") || filename.endsWith(".avi") || filename.endsWith(".mov")))
            return "video";
        else if (filename != null && (filename.endsWith(".jpeg")||filename.endsWith(".jpg") || filename.endsWith(".png")))
            return "image";
        else throw new FileException("File type unsupported");
    }


    public String fileType(int fileId)  throws FileException {
        Video video = videoRepository.findVideoById(fileId);
        if(video != null)
        {
            String videoName = video.getFileName();
            return videoName.substring(videoName.indexOf('.'));
        }
        Image image = imageRepository.findImageById(fileId);
        if(image != null)
        {
            String imageName = image.getFileName();
            return imageName.substring(imageName.indexOf('.'));
        }
        throw new FileException("File type unsupported");
    }



    public void deleteContent(Content contentToDelete, String albumToDeleteFrom) throws FileException, RepositoryException {
        String fileName =contentToDelete.getFileName();
        String filepath = createFilePath(albumToDeleteFrom, fileName);
        Path filePath = Paths.get(filepath);
        try {
             Files.deleteIfExists(filePath);
        }
        catch(IOException exception){
            throw new FileException("Couldn't delete file from disk");
        }
        try{
            if(contentToDelete instanceof Image){

                imageRepository.deleteById(contentToDelete.getFileId());
            }
            else if(contentToDelete instanceof Video)
                videoRepository.deleteById(contentToDelete.getFileId());
            Album album = albumRepository.findAlbumByTitle(albumToDeleteFrom);
            album.setAlbum_size(album.getAlbum_size()-contentToDelete.getFileSize());
            albumRepository.save(album);
            albumRepository.flush();
        }

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
        String filepath = createFilePath(albumName, filename);
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

    public File getFileFromPath(String filePath)
    {
        return new File(filePath);
    }

    public Path createFolder(String path) throws FileException {

        Path newFolderPath = Paths.get(path);
        try {
            Files.createDirectory(newFolderPath);
        }
        catch (IOException exception) {
            throw new FileException("Couldn't save album on disk");}
        return newFolderPath;
    }

    public List<File> getFilesFromIds(List<Integer> fileIds){
        List<File> files = new ArrayList<File>();
        fileIds.forEach(fileID->{
            try {
                Content content = getContentById(fileID);
                File file = getFileFromPath(content.getPath());
                files.add(file);
            } catch (Exception e) {

            }
        });
        return files;
    }

    public String zipContent(List<Integer> contentToZip){
        List<File> filesToZip = getFilesFromIds(contentToZip);
        String zipFilePath = createZipFilePath("tempZip.zip");
        try
        {
            deleteFileOrDirectory(zipFilePath);
        }
        catch (FileException exception){
        }

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {


            for (File file : filesToZip) {
                if (file.isDirectory()) {
                    // Handle directories if needed
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);

                // Use relative path as the entry name
                String entryPath = file.getName();
                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return zipFilePath;
    }
    public String zipFolder(String folderName) {
        String albumPath = createAlbumPath(folderName);
        String zipFilePath = createZipFilePath("tempZip.zip");
        try
        {
        deleteFileOrDirectory(zipFilePath);}
        catch (FileException exception){
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File sourceFile = new File(albumPath);

            // Check if the source file path is valid and the directory exists
            if (!sourceFile.exists() || !sourceFile.isDirectory()) {
                System.out.println("Invalid source file path: " + albumPath);
                return null;
            }

            for (File file : sourceFile.listFiles()) {
                if (file.isDirectory()) {
                    // Handle directories if needed
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);

                // Use relative path as the entry name
                String entryPath = file.getName();
                ZipEntry zipEntry = new ZipEntry(entryPath);
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return zipFilePath;
    }

    public void deleteFileOrDirectory(String filePath) throws FileException {
        try{
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);}
        catch(Exception exception){
            throw new FileException("Couldn't delete file");
        }
    }

    public List<String> retrieveFilePathsFromAlbum(String albumName){
        String albumPath = createAlbumPath(albumName);
        File album = new File(albumPath);
        File[] files = album.listFiles();
        List<String> filePaths = new ArrayList<String>();
        for(File file:files){
            filePaths.add(file.getAbsolutePath());
        }
        return filePaths;
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
        String folderPath = createAlbumPath(album.getAlbum_title());
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
        String thumbnailPath = createThumbnailPath(thumbnailName);
        Path pathToThumbnail = Paths.get(thumbnailPath);
        if(Files.exists(pathToThumbnail))
            return thumbnailPath;
        File thumbnail = new File(thumbnailPath);
        try{
            if(this.fileType(fileToThumbnail.getName())=="image"){
                Thumbnails.of(fileToThumbnail).size(width,height).toFile(thumbnail);
                return thumbnailPath;
            }
            else {
//                  couldnt get image from video.
//                //"D:\ServerFiles\main\Untitled video - Made with Clipchamp.mp4"
//                ImageStack stack = new AVI_Reader().makeStack(fileToThumbnail.getPath(), 1, 100,false, false, false);
//                ImageProcessor processor = stack.getProcessor(0);
//                java.awt.Image image = processor.createImage();
//                ImageIO.write((RenderedImage)image, "png", new File(thumbnailPath));
//                return thumbnailPath
                  return "D:\\ServerFiles\\Thumbnails\\thumbnail-anyVideo.png";
          }
        }
        catch(IOException exception)
        {
            throw new FileException("Couldn't save thumbnail on disk");
        }
    }

}
