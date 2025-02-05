package com.security.vinclub.repository;

import com.security.vinclub.dto.response.referencecode.GetReferenceCodeResponse;
import com.security.vinclub.entity.ReferenceCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenceCodeRepository extends JpaRepository<ReferenceCode, String> {
    String TABLE = "reference_code";

    @Query("SELECT NEW com.security.vinclub.dto.response.referencecode.GetReferenceCodeResponse(rc.id, rc.createdBy, u.username, u.phone, rc.referenceCode, rc.createdDate) " +
            "FROM " + TABLE + " rc JOIN user u ON u.id = rc.createdBy WHERE rc.deleted = false")
    Page<GetReferenceCodeResponse> findAllRefCodes(Pageable pageable);

    @Query("SELECT NEW com.security.vinclub.dto.response.referencecode.GetReferenceCodeResponse(rc.id, rc.createdBy, u.username, u.phone, rc.referenceCode, rc.createdDate) " +
            "FROM " + TABLE + " rc JOIN user u ON u.id = rc.createdBy " +
            "WHERE (u.username IS NULL OR LOWER(u.username) LIKE %:searchText%) " +
            "OR (rc.referenceCode IS NULL OR LOWER(rc.referenceCode) LIKE %:searchText%) " +
            "AND rc.deleted = false")
    Page<GetReferenceCodeResponse> searchAllRefCodes(String searchText, Pageable pageable);

    List<ReferenceCode> findByDeletedFalse();
}
