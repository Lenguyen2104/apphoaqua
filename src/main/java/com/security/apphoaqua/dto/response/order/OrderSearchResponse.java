package com.security.apphoaqua.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResponse {
    private String userName;
    private String orderCode;
    private String createdDate;
    private String status;
    private BigDecimal totalOrderAmount;
    private BigDecimal profit;
}
