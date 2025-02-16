package com.security.apphoaqua.dto.response.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class BankInfoToWithdrawResponse {
    @NotNull
    @JsonProperty("bank_id")
    private String bankId;

    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @NotNull
    @JsonProperty("account_name")
    private String accountName;

    @NotNull
    @JsonProperty("account_number")
    private String accountNumber;

    @NotNull
    @JsonProperty("bank_name")
    private String bankName;

    @NotNull
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
}
