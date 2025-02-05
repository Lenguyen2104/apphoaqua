package com.security.vinclub.dto.response.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String roleId;
    private String roleName;
    private String imageUrl;
    private String referenceCode;
    private BigDecimal totalAmount;
    private LocalDateTime lastDepositDate;
    private LocalDateTime lastWithdrawDate;
    private BigDecimal lastDepositAmount;
    private BigDecimal lastWithDrawAmount;
    private LocalDateTime createDate;
    private boolean activated;
}
