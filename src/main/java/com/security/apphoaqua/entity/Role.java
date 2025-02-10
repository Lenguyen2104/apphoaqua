package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.RoleRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = RoleRepository.TABLE)
public class Role {
    @Id
    @Column(nullable = false)
    private String id;
    @Column(unique = true, nullable = false)
    private String name;
}
