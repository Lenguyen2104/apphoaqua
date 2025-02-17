package com.security.apphoaqua.controller;

import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/un_auth/orders")
    public ResponseEntity<Object> createOrders(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/un_auth/orders/bill")
    public ResponseEntity<Object> billOrders(@RequestBody BillOrderRequest request) {
        return ResponseEntity.ok(orderService.billOrder(request));
    }
}
