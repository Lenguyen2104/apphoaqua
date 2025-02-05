package com.security.vinclub.dto.request.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "Tên tài khoản không được trống!")
    private String username;

    @NotBlank(message = "Mật khẩu không được trống!")
    private String password;
}
