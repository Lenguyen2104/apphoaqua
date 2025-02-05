package com.security.vinclub.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.entity.FileStorage;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.FileStorageRepository;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageRepository fileStorageRepository;
    private final UserRepository userRepository;

    private final String BASE_DIR = "avatars/";

    @Override
    public ResponseBody<Object> uploadAvatar(MultipartFile file, String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        if (file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

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

            user.setImageId(fileId);
            userRepository.save(user);

            var response = new ResponseBody<>();
            var json = new ObjectMapper().createObjectNode();
            json.putPOJO("file_id", fileId);
            response.setOperationSuccess(SUCCESS, json);
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
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
