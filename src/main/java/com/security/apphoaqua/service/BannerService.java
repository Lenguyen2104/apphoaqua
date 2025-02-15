package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BannerService {
    ResponseBody<Object> getAllBanner();

    ResponseBody<Object> updateBanner(MultipartFile file, String id, String name);

    byte[] downloadOriginalWithUrl(String fileId) throws IOException;
}
