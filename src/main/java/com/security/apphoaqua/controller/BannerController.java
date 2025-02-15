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

    @GetMapping("/un_auth/banner/all")
    public ResponseEntity<Object> getAllBanner() {
        return ResponseEntity.ok(bannerService.getAllBanner());
    }

    @PostMapping(value = "/admin/banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateBanner(@RequestParam(value = "file", required = false) MultipartFile file,
                                          @RequestParam(value  = "id") String id,
                                          @RequestParam(value = "name", required = false) String name) throws IOException {
        return ResponseEntity.ok(bannerService.updateBanner(file, id, name));
    }

    @GetMapping(value = "/un_auth/banner/file/download/{fileId}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> downloadFileOriginalWithUrl(@PathVariable String fileId) throws IOException {
        return ResponseEntity.ok(bannerService.downloadOriginalWithUrl(fileId));
    }

}
