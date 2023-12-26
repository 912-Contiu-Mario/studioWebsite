package com.example.Repository;

import com.example.Model.Image;
import com.example.Model.User;
import com.example.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i FROM Image i WHERE i.image_name = :image_name")
    Image findImageByImageName(@Param("image_name") String image_name);

    @Query("SELECT i FROM Image i WHERE i.album_id = :album_id")
    List<Image> findImagesByAlbumId(@Param("album_id") Integer album_id);

}