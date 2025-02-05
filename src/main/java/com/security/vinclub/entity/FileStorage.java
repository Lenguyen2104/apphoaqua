package com.security.vinclub.entity;

import com.security.vinclub.repository.FileStorageRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = FileStorageRepository.TABLE)
public class FileStorage {
    @Id
    @Column(nullable = false)
    private String id;
    @Column(name = "file_directory")
    private String fileDirectory;
    @Column(name = "raw_file_name")
    private String rawFileName;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_extension")
    private String fileExtension;
    @Column(name = "description")
    private String description;
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}
