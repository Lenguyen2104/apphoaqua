package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    String TABLE = "orders";

}
