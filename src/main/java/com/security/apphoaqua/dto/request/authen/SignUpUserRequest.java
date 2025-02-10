package com.security.apphoaqua.dto.request.authen;

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

    @NotBlank(message = "Số điện thoại không được trống!")
    private String phone;

}
