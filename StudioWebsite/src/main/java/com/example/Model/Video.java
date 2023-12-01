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
public class Video {
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

    @Column(insertable = false, name = "upload_date")
    private Date upload_date;

    public Video(int album_id, String video_name, String video_path) {
        this.album_id = album_id;
        this.video_name = video_name;
        this.video_path = video_path;
    }
}
