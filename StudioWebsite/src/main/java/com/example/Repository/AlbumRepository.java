package com.example.Repository;

import com.example.Model.Album;
import com.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    @Query("SELECT a FROM Album a WHERE a.album_title = :album_title")
    Album findAlbumByTitle(@Param("album_title") String album_title);

}
