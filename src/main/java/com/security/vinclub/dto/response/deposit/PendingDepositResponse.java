package com.security.vinclub.dto.response.deposit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
public class PendingDepositResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("status")
    private AppovalStatusEnum status = AppovalStatusEnum.PENDING;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("bank_name")
    private String bankName;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    public PendingDepositResponse(String id, String userId, AppovalStatusEnum status, String accountNumber, String accountName, String bankName, BigDecimal amount, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.bankName = bankName;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
