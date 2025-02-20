package com.security.apphoaqua.entity;

import com.security.apphoaqua.enumeration.AppovalStatusEnum;
import com.security.apphoaqua.repository.DepositRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = DepositRepository.TABLE)
public class Deposit {
    @Id
    private String id;
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;
    @Column(name = "status", length = 50, nullable = false)
    private AppovalStatusEnum status = AppovalStatusEnum.PENDING;
    @Column(name = "account_number", length = 50)
    private String accountNumber;
    @Column(name = "account_name", length = 50)
    private String accountName;
    @Column(name = "bank_name", length = 50)
    private String bankName;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
