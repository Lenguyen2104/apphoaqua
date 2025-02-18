package com.security.apphoaqua.controller;

import com.security.apphoaqua.dto.request.order.AllOrderRequest;
import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.dto.request.order.OrderSearchRequest;
import com.security.apphoaqua.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/un_auth/orders/todays/{user_id}")
    public ResponseEntity<Object> todayOrders(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(orderService.todayOrder(userId));
    }

    @PostMapping("/un_auth/orders/all")
    public ResponseEntity<Object> allOrders(@RequestBody AllOrderRequest request) {
        return ResponseEntity.ok(orderService.allOrders(request));
    }

    @PostMapping("/un_auth/order/get_all")
    public ResponseEntity<Object> getAllOrder(@RequestBody OrderSearchRequest request) {
        return ResponseEntity.ok(orderService.getAllOrders(request));
    }
}
