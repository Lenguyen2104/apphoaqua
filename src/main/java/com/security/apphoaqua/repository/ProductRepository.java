package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    String TABLE = "products";

    boolean existsByProductName(String productName);
}
