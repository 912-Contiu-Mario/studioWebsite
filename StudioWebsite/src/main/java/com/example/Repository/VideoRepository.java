package com.example.Repository;

import com.example.Model.Image;
import com.example.Model.User;
import com.example.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query("SELECT v FROM Video v WHERE v.video_name = :video_name")
    Video findVideoByVideoName(@Param("video_name") String video_name);

    @Query("SELECT v FROM Video v WHERE v.id = :id")
    Video findVideoById(@Param("id") int id);

    @Query("SELECT v FROM Video v WHERE v.video_name = :video_name AND v.album_id = :album_id")
    Video findVideoByVideoNameAndAlbumId(@Param("video_name") String video_name, @Param("album_id")int album_id);
    @Query("SELECT v FROM Video v WHERE v.album_id = :album_id")
    List<Video> findVideosByAlbumId(@Param("album_id") Integer album_id);
}
