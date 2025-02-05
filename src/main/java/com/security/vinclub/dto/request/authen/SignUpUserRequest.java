package com.security.vinclub.dto.request.authen;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpUserRequest {

    @NotBlank(message = "Tên tài khoản không được trống!")
    private String username;

    @NotBlank(message = "Mật khẩu không được trống!")
    private String password;

    private String email;

    @NotBlank(message = "Họ & tên khoản không được trống!")
    private String fullName;

    @NotBlank(message = "Mã tham chiếu không được trống!")
    private String referenceCode;

    @NotBlank(message = "Số điện thoại không được trống!")
    private String phone;

}
