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
public class TodayOrderResponse {
    private BigDecimal accountBalance;
    private BigDecimal profitToday;
    private Integer numberOfCompleteApplications;
    private BigDecimal pendingAmount;
}
