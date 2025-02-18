package com.security.apphoaqua.dto.response.order;

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
public class BillOrderResponse {
    private String orderId;
    private String userId;
    private String productId;
    private String productImage;
    private String status;
    private LocalDateTime receivedTime;
    private BigDecimal totalOrderAmount;
    private BigDecimal profit;
    private BigDecimal totalRefundAmount;
    private Double interestRate;
    private String orderCode;
}
