package com.security.apphoaqua.dto.request.authen;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Token is not blank")
    private String token;
}
