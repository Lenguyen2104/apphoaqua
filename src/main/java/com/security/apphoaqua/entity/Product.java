package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.ProductRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = ProductRepository.TABLE)
public class Product {

    @Id
    private String productId;

    private String productName;

    private BigDecimal price;

    @Column(name = "interest_rate")
    private Double interestRate;

    private String imageId;

    private String level;
}

