package com.security.apphoaqua.dto.request.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateBankRequest {

    @NotBlank(message = "Số điện thoại không được để trống")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Họ và tên không được để trống")
    @JsonProperty("account_name")
    private String accountName;

    @NotBlank(message = "Số tài khoản không được để trống")
    @JsonProperty("account_number")
    private String accountNumber;

    @NotBlank(message = "Ngân hàng không được để trống")
    @JsonProperty("bank_name")
    private String bankName;

    @NotBlank(message = "Chi nhánh ngân hàng không được để trống")
    @JsonProperty("address")
    private String address;
}
