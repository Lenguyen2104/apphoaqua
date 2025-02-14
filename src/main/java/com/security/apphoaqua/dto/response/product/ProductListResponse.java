package com.security.apphoaqua.dto.response.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {

    @JsonProperty("product_id")
    private String productId;

    private String name;

    private BigDecimal price;

    @JsonProperty("interest_rate")
    private Double interestRate;

    private String imageId;

    private String level;

    private String status;
}

