package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.VideoSlider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoSliderRepository extends JpaRepository<VideoSlider, String> {
    String TABLE = "video_slider";
}
