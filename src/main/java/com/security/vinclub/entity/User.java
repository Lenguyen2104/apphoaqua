package com.security.vinclub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.vinclub.common.SecurityContext;
import com.security.vinclub.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = UserRepository.TABLE)
public class User implements UserDetails {

    @Id
    private String id;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", length = 50, nullable = false)
    private String fullName;

    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String phone;

    @Column(name = "reference_code", nullable = false)
    private String referenceCode;

    @Email
    @Column(length = 254)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @NotNull
    @Column(nullable = false)
    private boolean deleted = false;

    @Size(max = 256)
    @Column(name = "image_id", length = 256)
    private String imageId;

    @Size(max = 50)
    @Column(name = "role_id", length = 50)
    private String roleId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "last_deposit_date")
    private LocalDateTime lastDepositDate;

    @Column(name = "last_withdraw_date")
    private LocalDateTime lastWithdrawDate;

    @Column(name = "last_deposit_amount")
    private BigDecimal lastDepositAmount;

    @Column(name = "last_withDraw_amount")
    private BigDecimal lastWithDrawAmount;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    protected User getCurrentUser() {
        return SecurityContext.getCurrentUser();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleId));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
