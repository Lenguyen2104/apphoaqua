package com.security.vinclub.dto.request.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    @NotBlank(message = "ID người dùng không được để trống")
    @JsonProperty("user_id")
    private String userId;

    @NotBlank(message = "Tên không được để trống")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email phải hợp lệ")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Vai trò không được để trống")
    @JsonProperty("role_id")
    private String roleId;
}
