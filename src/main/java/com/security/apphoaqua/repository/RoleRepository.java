package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    String TABLE = "role";

    Role findByName(String role);

    List<Role> findByIdIn(List<String> roleIds);
}
