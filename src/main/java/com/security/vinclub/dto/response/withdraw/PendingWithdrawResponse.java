package com.security.vinclub.dto.response.withdraw;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
public class PendingWithdrawResponse {
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
}
