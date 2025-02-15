package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, String> {
    String TABLE = "banner";
}
