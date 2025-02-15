package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.entity.Banner;
import com.security.apphoaqua.entity.Role;
import com.security.apphoaqua.entity.User;
import com.security.apphoaqua.repository.BannerRepository;
import com.security.apphoaqua.repository.RoleRepository;
import com.security.apphoaqua.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        log.info("Khởi tạo dữ liệu hệ thống...");

        initRoles();

        initAdminUser();

        initBanners();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(UUID.randomUUID().toString().replaceAll("-", ""), "ADMIN"));
            roleRepository.save(new Role(UUID.randomUUID().toString().replaceAll("-", ""), "USER"));
            log.info("Đã tạo các role mặc định: ADMIN, USER");
        }
    }

    private void initAdminUser() {
        if (userRepository.count() == 0) {
            log.info("Database chưa có tài khoản user nào, tiến hành tạo tài khoản admin...");

            Optional<Role> adminRole = Optional.ofNullable(roleRepository.findByName("ADMIN"));

            if (adminRole.isPresent()) {
                User adminUser = new User();
                adminUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin")); // Mã hóa mật khẩu
                adminUser.setFullName("Admin User");
                adminUser.setPhone("0123456789");
                adminUser.setEmail("admin@example.com");
                adminUser.setActivated(true);
                adminUser.setDeleted(false);
                adminUser.setImageId(null);
                adminUser.setRoleId(adminRole.get().getId());
                adminUser.setTotalAmount(BigDecimal.ZERO);
                adminUser.setCreatedDate(LocalDateTime.now());

                adminUser.setShareholderLevel1(true);
                adminUser.setShareholderLevel2(false);
                adminUser.setShareholderLevel3(false);
                adminUser.setShareholderLevel4(false);

                userRepository.save(adminUser);
                log.info("Tài khoản admin đã được tạo thành công với username: admin");
            } else {
                log.warn("Role ADMIN chưa được tạo, không thể tạo tài khoản admin.");
            }
        } else {
            log.info("Database đã có tài khoản user, không cần tạo tài khoản admin.");
        }
    }

    private void initBanners() {
        if (bannerRepository.count() == 0) {
            log.info("Bảng Banner chưa có dữ liệu, tiến hành tạo 3 banner mặc định...");

            List<Banner> banners = List.of(
                    new Banner(UUID.randomUUID().toString(), "CỔ ĐÔNG", "https://example.com/banner1.jpg", true, false),
                    new Banner(UUID.randomUUID().toString(), "THANH TOÁN CHUYỂN HÀNG", "https://example.com/banner2.jpg", true, false),
                    new Banner(UUID.randomUUID().toString(), "RÚT LỢI NHUẬN CHUYỂN HÀNG", "https://example.com/banner3.jpg", true, false)
            );

            bannerRepository.saveAll(banners);
            log.info("Đã tạo 3 banner mặc định thành công.");
        } else {
            log.info("Bảng Banner đã có dữ liệu, không cần tạo thêm.");
        }
    }
}
