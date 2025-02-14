package com.security.apphoaqua.controller;

import com.security.apphoaqua.dto.request.product.CreateProductRequest;
import com.security.apphoaqua.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping("/products/create")
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }
}
