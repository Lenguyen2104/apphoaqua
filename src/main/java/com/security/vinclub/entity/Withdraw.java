package com.security.vinclub.entity;

import com.security.vinclub.enumeration.AppovalStatusEnum;
import com.security.vinclub.repository.WithdrawRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = WithdrawRepository.TABLE)
public class Withdraw {
    @Id
    private String id;
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;
    @Column(name = "status", length = 50, nullable = false)
    private AppovalStatusEnum status = AppovalStatusEnum.PENDING;
    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;
    @Column(name = "account_name", length = 50, nullable = false)
    private String accountName;
    @Column(name = "bank_name", length = 50, nullable = false)
    private String bankName;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
