package com.example.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="Albums")
public class Album {

    @Id
    @Column(name = "album_id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "album_title")
    private String album_title;

    @Column(name= "creation_date")
    private Date creation_date;
}
