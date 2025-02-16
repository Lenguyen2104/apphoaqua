package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.BankRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = BankRepository.TABLE)
public class Bank {
    @Id
    private String id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "phone", length = 256)
    private String phone;

    @Column(name = "account_name", length = 256)
    private String accountName;

    @Column(name = "account_number", length = 256)
    private String accountNumber;

    @Column(name = "bank_name", length = 256)
    private String bankName;

    @Column(name = "address", length = 256)
    private String address;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @NotNull
    @Column(nullable = false)
    private boolean deleted = false;

    public Bank(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }
}
