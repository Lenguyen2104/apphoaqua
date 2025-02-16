package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.entity.Order;
import com.security.apphoaqua.repository.OrderRepository;
import com.security.apphoaqua.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public ResponseBody<Object> createOrder(CreateOrderRequest request) {
        var orderId = UUID.randomUUID().toString().replaceAll("-", "");
        var orderModel = Order.builder()
                .orderId(orderId)
                .productImage(request.getProductImage())
                .status(request.getStatus())
                .receivedTime(request.getReceivedTime())
                .totalOrderAmount(request.getTotalOrderAmount())
                .profit(request.getProfit())
                .totalRefundAmount(request.getTotalRefundAmount())
                .build();
        orderRepository.save(orderModel);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, "productListResponse");
        return response;
    }
}
