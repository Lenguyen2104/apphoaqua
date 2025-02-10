package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.entity.Role;
import com.security.apphoaqua.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(UUID.randomUUID().toString().replaceAll("-", ""), "ADMIN"));
            roleRepository.save(new Role(UUID.randomUUID().toString().replaceAll("-", ""), "USER"));
        }
    }
}
