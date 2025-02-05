package com.security.vinclub.entity;

import com.security.vinclub.repository.ReferenceCodeRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = ReferenceCodeRepository.TABLE)
public class ReferenceCode {
    @Id
    private String id;
    @Column(name = "reference_code", nullable = false, unique = true)
    private String referenceCode;
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private boolean deleted = false;
}
