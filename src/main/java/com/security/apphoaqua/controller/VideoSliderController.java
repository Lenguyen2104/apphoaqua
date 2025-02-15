package com.security.apphoaqua.controller;

import com.security.apphoaqua.service.VideoSliderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VideoSliderController {
    private final VideoSliderService videoSliderService;
    @GetMapping("/un_auth/video-slider/all")
    public ResponseEntity<Object> getAllBanner() {
        return ResponseEntity.ok(videoSliderService.getAllVideoSlider());
    }

    @PutMapping(value = "/admin/video-slider", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateVideoSlider(@RequestParam(value = "file", required = false) MultipartFile file,
                                               @RequestParam(value  = "id") String id,
                                               @RequestParam(value = "name", required = false) String name) throws IOException {
        return ResponseEntity.ok(videoSliderService.updateVideoSlider(file, id, name));
    }

    @PostMapping(value = "/admin/video-slider", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createVideoSlider(@RequestParam(value = "file", required = false) MultipartFile file,
                                             @RequestParam(value = "name") String name) throws IOException {
        return ResponseEntity.ok(videoSliderService.createVideoSlider(file, name));
    }

    @DeleteMapping(value = "/admin/video-slider/{id}")
    public ResponseEntity<?> deleteVideoSlider(@PathVariable(value = "id") String id) throws IOException {
        return ResponseEntity.ok(videoSliderService.deleteVideoSlider(id));
    }
}
