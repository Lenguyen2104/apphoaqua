package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.ProductRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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
    private int level;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}

