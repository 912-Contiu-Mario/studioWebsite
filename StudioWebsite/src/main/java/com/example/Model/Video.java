package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Videos")
public class Video implements Content{
    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "album_id")
    private Integer album_id;


    @Column(name = "video_name")
    private String video_name;

    @Column(name = "video_path")
    private String video_path;


    @Column(name = "video_uploader")
    private String video_uploader;

    @Column(name="video_size")
    private float video_size;

    @Column(insertable = false, name = "upload_date")
    private Date upload_date;





    public Video(int album_id, String video_name, String video_path, String video_uploader, float video_size) {
        this.album_id = album_id;
        this.video_name = video_name;
        this.video_path = video_path;
        this.video_uploader=video_uploader;
        this.video_size=video_size;
    }



    @Override
    public String getFileType()
    {
        return "video";
    }


    @Override

    public String getPath()
    {
        return video_path;
    }


    @Override
    public int getFileId()
    {
        return this.id;
    }

    @Override
    public Date getUploadDate() {
        return upload_date;
    }

    @Override
    public String getFileName() {
        return video_name;
    }

    @Override
    public String getFileUploader() {
        return video_uploader;
    }

    @Override
    public float getFileSize() {
        return video_size;
    }

}
