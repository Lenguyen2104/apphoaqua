package com.security.vinclub.dto.request.deposit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateDepositRequest {
    @NotBlank(message = "ID người dùng không được để trống")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "Số tài khoản không được để trống")
    @JsonProperty("account_number")
    private String accountNumber;

    @NotBlank(message = "Chủ sở hữu không được để trống")
    @JsonProperty("account_name")
    private String accountName;

    @NotBlank(message = "Tên ngân hàng không được để trống")
    @JsonProperty("bank_name")
    private String bankName;

    @NotNull(message = "Số tiền không được để trống")
    @JsonProperty("amount")
    private BigDecimal amount;
}
