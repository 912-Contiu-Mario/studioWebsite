package com.example.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.CachePut;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Images")
public class Image implements Content{
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "album_id")
    private Integer album_id;


    @Column(name = "image_name")
    private String image_name;

    @Column(name = "image_path")
    private String image_path;


    @Column(name = "image_uploader")
    private String image_uploader;

    @Column(name="image_size")
    private float image_size;

    @Column(insertable = false, name = "upload_date")
    private Date upload_date;




    public Image(int album_id, String image_name, String image_path, String image_uploader, float image_size) {
        this.album_id = album_id;
        this.image_name = image_name;
        this.image_path = image_path;
        this.image_uploader = image_uploader;
        this.image_size=image_size;
    }
    @Override
    public String getFileType()
    {
        return "image";
    }


    @Override
    public String getPath()
    {
        return this.image_path;
    }
    @Override
    public Date getUploadDate()
    {
        return this.upload_date;
    }

    @Override
    public String getFileName() {
        return image_name;
    }

    @Override
    public int getFileId()
    {
        return this.id;
    }

    @Override
    public String getFileUploader()
    {
        return this.image_uploader;
    }

    @Override
    public float getFileSize() {
        return image_size;
    }


}
