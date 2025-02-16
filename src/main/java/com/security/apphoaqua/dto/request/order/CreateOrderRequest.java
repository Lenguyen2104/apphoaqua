package com.security.apphoaqua.dto.request.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrderRequest {
    private String productImage;
    private String status;
    private LocalDateTime receivedTime;
    private BigDecimal totalOrderAmount;
    private BigDecimal profit;
    private BigDecimal totalRefundAmount;
}
