package com.security.apphoaqua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.banner.UpdateBannerRequest;
import com.security.apphoaqua.dto.response.banner.BannerAdminResponse;
import com.security.apphoaqua.entity.Banner;
import com.security.apphoaqua.entity.FileStorage;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.BannerRepository;
import com.security.apphoaqua.repository.FileStorageRepository;
import com.security.apphoaqua.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final FileStorageRepository fileStorageRepository;
    private final String BASE_DIR = "banner/";
    @Override
    public ResponseBody<Object> getAllBanner() {
        List<Banner> banners = bannerRepository.findAll();

        List<BannerAdminResponse> bannerAdminResponses = banners.stream()
                .map(banner -> BannerAdminResponse.builder()
                        .id(banner.getId())
                        .name(banner.getName())
                        .fileId(banner.getFileId())
                        .spot(banner.getSpot())
                        .activated(banner.isActivated())
                        .deleted(banner.isDeleted())
                        .build())
                .sorted(Comparator.comparing(BannerAdminResponse::getSpot))
                .toList();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, bannerAdminResponses);
        return response;
    }

    @Override
    public ResponseBody<Object> updateBanner(MultipartFile file, String id, String name) {
        var banner = bannerRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(BANNER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, BANNER_NOT_FOUND, errorMapping);
        });

        String oldFileId = banner.getFileId(); // 🔥 Lưu fileId cũ trước khi cập nhật

        if (name != null && !banner.getName().equals(name)) {
            banner.setName(name);
        }

        if (file != null && !file.isEmpty()) {
            try {
                String fileId = UUID.randomUUID().toString();
                String fileName = fileId + "_" + file.getOriginalFilename();
                Path path = Paths.get(BASE_DIR + fileName);

                if (!Files.exists(Paths.get(BASE_DIR))) {
                    Files.createDirectories(Paths.get(BASE_DIR));
                }

                file.transferTo(path);

                FileStorage fileStorage = new FileStorage();
                fileStorage.setId(fileId);
                fileStorage.setFileDirectory(path.toString());
                fileStorage.setRawFileName(file.getOriginalFilename());
                fileStorage.setFileName(fileName);
                fileStorage.setFileExtension(getFileExtension(Objects.requireNonNull(file.getOriginalFilename())));
                fileStorage.setCreateDate(LocalDateTime.now());
                fileStorageRepository.save(fileStorage);

                banner.setFileId(fileId);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage());
            }
        }

        bannerRepository.save(banner); // 🔥 Lưu banner vào DB trước

        if (oldFileId != null) {
            var oldFile = fileStorageRepository.findById(oldFileId);
            if (oldFile.isPresent()) {
                try {
                    Path oldFilePath = Paths.get(oldFile.get().getFileDirectory());
                    Files.deleteIfExists(oldFilePath);
                    fileStorageRepository.deleteById(oldFileId);
                } catch (IOException e) {
                    System.err.println("🚨 Lỗi khi xóa file cũ: " + e.getMessage());
                }
            }
        }

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, banner);
        return response;
    }


    @Override
    public byte[] downloadOriginalWithUrl(String fileId) throws IOException {
        FileStorage fileStorageModel = fileStorageRepository.findById(fileId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(FILE_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, FILE_NOT_FOUND, errorMapping);
        });
        String fileDirectory = fileStorageModel.getFileDirectory();

        File file = new File(fileDirectory);
        byte[] bytes = Files.readAllBytes(file.toPath());
        HttpHeaders headers = getHttpHeaders(fileStorageModel.getRawFileName());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes).getBody();
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }

    private static HttpHeaders getHttpHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }

}
