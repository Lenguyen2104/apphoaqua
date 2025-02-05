package com.security.vinclub.dto.response.referencecode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class GetReferenceCodeResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("created_by")
    private String created_by;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("reference_code")
    private String referenceCode;
    @JsonProperty("created_date")
    private LocalDateTime createdDate;
}
