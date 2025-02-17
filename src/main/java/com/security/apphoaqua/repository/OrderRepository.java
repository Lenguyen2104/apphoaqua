package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    String TABLE = "orders";
    Optional<Order> findByUserIdAndProductIdAndStatus(String userId, String productId, String status);
    void deleteAllByProductIdAndStatus(String productId, String status);
    List<Order> findAllByProductId(String productId);
}
