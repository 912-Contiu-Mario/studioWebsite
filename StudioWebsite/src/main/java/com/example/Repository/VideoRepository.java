package com.example.Repository;

import com.example.Model.User;
import com.example.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query("SELECT v FROM Video v WHERE v.video_name =: video_name")
    User findImageByImageName(@Param("video_name") String video_name);
}
