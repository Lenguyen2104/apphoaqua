package com.security.apphoaqua.dto.response.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class TransactionDetailResponse {
    @JsonProperty("description")
    private String description;
    @JsonProperty("date")
    private LocalDateTime date;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
}
