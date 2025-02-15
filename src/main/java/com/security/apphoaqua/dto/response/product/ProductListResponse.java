package com.security.apphoaqua.dto.response.product;


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
public class ProductListResponse {

    private String productId;
    private String productName;
    private BigDecimal price;
    private Double interestRate;
    private String imageId;
    private int level;
    private LocalDateTime createDate;
}

