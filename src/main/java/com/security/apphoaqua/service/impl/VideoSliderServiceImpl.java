package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.response.videoslider.VideoSliderAdminResponse;
import com.security.apphoaqua.entity.FileStorage;
import com.security.apphoaqua.entity.VideoSlider;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.FileStorageRepository;
import com.security.apphoaqua.repository.VideoSliderRepository;
import com.security.apphoaqua.service.VideoSliderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
public class VideoSliderServiceImpl implements VideoSliderService {
    private final VideoSliderRepository videoSliderRepository;
    private final FileStorageRepository fileStorageRepository;
    private final String BASE_DIR = "video/";
    @Override
    public ResponseBody<Object> getAllVideoSlider() {
        List<VideoSlider> videoSliders = videoSliderRepository.findAll();

        List<VideoSliderAdminResponse> videoSliderAdminResponses = videoSliders.stream()
                .map(videoSlider -> VideoSliderAdminResponse.builder()
                        .id(videoSlider.getId())
                        .name(videoSlider.getName())
                        .fileId(videoSlider.getFileId())
                        .createdDate(videoSlider.getCreatedDate())
                        .build())
                .sorted(Comparator.comparing(VideoSliderAdminResponse::getCreatedDate))
                .toList();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, videoSliderAdminResponses);
        return response;
    }

    @Override
    public ResponseBody<Object> updateVideoSlider(MultipartFile file, String id, String name) {
        var videoSlider = videoSliderRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(VIDEO_SLIDER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, VIDEO_SLIDER_NOT_FOUND, errorMapping);
        });

        String oldFileId = videoSlider.getFileId();

        if (name != null && !videoSlider.getName().equals(name)) {
            videoSlider.setName(name);
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

                videoSlider.setFileId(fileId);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage());
            }
        }

        videoSliderRepository.save(videoSlider);

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
        response.setOperationSuccess(SUCCESS, videoSlider);
        return response;
    }

    @Override
    public ResponseBody<Object> createVideoSlider(MultipartFile file, String name) {
        VideoSlider videoSlider = new VideoSlider();
        videoSlider.setId(UUID.randomUUID().toString());
        videoSlider.setName(name);

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

                videoSlider.setFileId(fileId);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + e.getMessage());
            }
        }

        videoSliderRepository.save(videoSlider);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, videoSlider);
        return response;
    }

    @Override
    public ResponseBody<Object> deleteVideoSlider(String id) {
        var videoSlider = videoSliderRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(VIDEO_SLIDER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, VIDEO_SLIDER_NOT_FOUND, errorMapping);
        });

        String oldFileId = videoSlider.getFileId();

        videoSliderRepository.deleteById(id);

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
        response.setOperationSuccess(SUCCESS, id);
        return response;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }
}
