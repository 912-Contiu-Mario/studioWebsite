package com.example.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="Albums")
public class Album {

    @Id
    @Column(name = "album_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "album_title")
    private String album_title;

    @Column(insertable = false, name = "creation_date")
    private Date creation_date;

    @Column(name="album_size")
    private float album_size;
}
