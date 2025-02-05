package com.security.vinclub.repository;

import com.security.vinclub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    String TABLE = "role";

    Role findByName(String role);

    List<Role> findByIdIn(List<String> roleIds);
}
