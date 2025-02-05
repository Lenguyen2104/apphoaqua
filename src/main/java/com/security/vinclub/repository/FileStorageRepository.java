package com.security.vinclub.repository;

import com.security.vinclub.entity.FileStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileStorage, String> {
    String TABLE = "file_storage";

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE fileId IN (:fileIds)", nativeQuery = true)
    List<FileStorage> findAllByFileId(@Param("fileIds") List<String> fileIds);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE fileType IN (:type)", nativeQuery = true)
    List<FileStorage> findAllByFileType(@Param("type") List<String> type);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE fileName LIKE %:fileName% AND fileType LIKE %:fileType% ", nativeQuery = true)
    Page<FileStorage> findByFileNameAndFileType(@Param("fileName") String fileName, @Param("fileType") String fileType, Pageable pageable);
}
