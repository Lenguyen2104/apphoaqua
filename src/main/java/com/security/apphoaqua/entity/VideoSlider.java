package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.VideoSliderRepository;
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
@Entity(name = VideoSliderRepository.TABLE)
public class VideoSlider {
    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "file_id", length = 256)
    private String fileId;

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @NotNull
    @Column(nullable = false)
    private boolean deleted = false;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
}
