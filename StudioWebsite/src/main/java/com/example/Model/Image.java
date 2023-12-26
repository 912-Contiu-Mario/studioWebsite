package com.example.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(insertable = false, name = "upload_date")
    private Date upload_date;

    public Image(int album_id, String image_name, String image_path) {
        this.album_id = album_id;
        this.image_name = image_name;
        this.image_path = image_path;
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
}
