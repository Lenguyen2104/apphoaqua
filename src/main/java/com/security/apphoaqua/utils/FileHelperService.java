package com.security.apphoaqua.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHelperService {
    public byte[] readAllBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }
    public boolean deleteIfExists(Path filePath) throws IOException {
        return Files.deleteIfExists(filePath);
    }
}
