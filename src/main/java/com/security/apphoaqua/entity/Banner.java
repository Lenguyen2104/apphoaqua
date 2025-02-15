package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.BannerRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = BannerRepository.TABLE)
public class Banner {
    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "file_id", length = 256)
    private String fileId;

    @Column(name = "spot")
    private String spot;

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @NotNull
    @Column(nullable = false)
    private boolean deleted = false;

}
