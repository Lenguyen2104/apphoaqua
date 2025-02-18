package com.security.apphoaqua.dto.request.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Mật khẩu cũ không được trống!")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được trống!")
    private String newPassword;
}
