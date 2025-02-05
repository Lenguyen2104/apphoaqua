package com.security.vinclub.dto.request.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Tên tài khoản không được trống!")
    private String username;

    @NotBlank(message = "Mật khẩu cũ không được trống!")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được trống!")
    private String newPassword;
}
