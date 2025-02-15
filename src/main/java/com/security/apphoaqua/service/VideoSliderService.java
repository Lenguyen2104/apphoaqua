package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface VideoSliderService {
    ResponseBody<Object> getAllVideoSlider();

    ResponseBody<Object> updateVideoSlider(MultipartFile file, String id, String name);

    ResponseBody<Object> createVideoSlider(MultipartFile file, String name);

    ResponseBody<Object> deleteVideoSlider(String id);
}
