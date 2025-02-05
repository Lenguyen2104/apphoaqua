package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    ResponseBody<Object> uploadAvatar(MultipartFile file, String userId);

    byte[] downloadOriginalWithUrl(String fileId) throws IOException;
}
