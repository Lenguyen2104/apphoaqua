package com.security.apphoaqua.controller;

import com.security.apphoaqua.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/admin/banner/all")
    public ResponseEntity<Object> getAllBanner() {
        return ResponseEntity.ok(bannerService.getAllBanner());
    }

}
