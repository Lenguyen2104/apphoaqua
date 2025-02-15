package com.security.apphoaqua.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.apphoaqua.repository.BannerRepository;
import com.security.apphoaqua.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "url", length = 256)
    private String url;

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @NotNull
    @Column(nullable = false)
    private boolean deleted = false;

}
