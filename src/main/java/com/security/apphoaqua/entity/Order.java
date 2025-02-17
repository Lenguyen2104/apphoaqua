package com.security.apphoaqua.entity;

import com.security.apphoaqua.repository.OrderRepository;
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
@Entity(name = OrderRepository.TABLE)
public class Order {

    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "received_time")
    private LocalDateTime receivedTime;

    @Column(name = "product_image")
    private String productImage;

    @Column(name = "total_order_amount")
    private BigDecimal totalOrderAmount;

    @Column(name = "profit")
    private BigDecimal profit;

    @Column(name = "total_refund_amount")
    private BigDecimal totalRefundAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}
