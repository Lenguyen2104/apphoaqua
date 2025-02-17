package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.dto.response.order.BillOrderResponse;
import com.security.apphoaqua.dto.response.order.OrderResponse;
import com.security.apphoaqua.entity.Order;
import com.security.apphoaqua.enumeration.StatusProduct;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.OrderRepository;
import com.security.apphoaqua.repository.ProductRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createOrder(CreateOrderRequest request) {
        var orderResponse = new OrderResponse();
        var productModel = productRepository.findById(request.getProductId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });
        BigDecimal rate = BigDecimal.valueOf(productModel.getInterestRate()).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        var profit = request.getTotalOrderAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);
        var order = orderRepository.findByUserIdAndProductIdAndStatus(
                request.getUserId(), request.getProductId(), StatusProduct.WAIT_FOR_COMPLETION.getValue());
        if (order.isPresent()) {
            orderResponse = OrderResponse.builder()
                    .orderId(order.get().getOrderId())
                    .userId(order.get().getUserId())
                    .productId(order.get().getProductId())
                    .productImage(order.get().getProductImage())
                    .status(order.get().getStatus())
                    .receivedTime(order.get().getReceivedTime())
                    .totalOrderAmount(request.getTotalOrderAmount())
                    .profit(profit)
                    .totalRefundAmount(request.getTotalOrderAmount().add(profit))
                    .interestRate(productModel.getInterestRate())
                    .build();
        } else {

            var orderModel = Order.builder()
                    .orderId(UUID.randomUUID().toString().replaceAll("-", ""))
                    .userId(request.getUserId())
                    .productId(request.getProductId())
                    .productImage(productModel.getImageId())
                    .status(StatusProduct.WAIT_FOR_COMPLETION.getValue())
                    .totalOrderAmount(request.getTotalOrderAmount())
                    .profit(profit)
                    .totalRefundAmount(request.getTotalOrderAmount().add(profit))
                    .createDate(LocalDateTime.now())
                    .build();
            orderRepository.save(orderModel);
            orderResponse = OrderResponse.builder()
                    .orderId(orderModel.getOrderId())
                    .userId(orderModel.getUserId())
                    .productId(orderModel.getProductId())
                    .productImage(orderModel.getProductImage())
                    .status(orderModel.getStatus())
                    .totalOrderAmount(orderModel.getTotalOrderAmount())
                    .profit(orderModel.getProfit())
                    .totalRefundAmount(orderModel.getTotalRefundAmount())
                    .interestRate(productModel.getInterestRate())
                    .build();
        }
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, orderResponse);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> billOrder(BillOrderRequest request) {
        var order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, ORDER_NOT_FOUND, errorMapping);
        });
        var productModel = productRepository.findById(order.getProductId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });
        var user = userRepository.findById(order.getUserId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        if (user.getTotalAmount().compareTo(order.getTotalOrderAmount()) < 0) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(INSUFFICIENT_BALANCE.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, INSUFFICIENT_BALANCE, errorMapping);
        }

        BigDecimal rate = BigDecimal.valueOf(productModel.getInterestRate()).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        var profit = request.getTotalOrderAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);
        var totalRefundAmount = request.getTotalOrderAmount().add(profit);
        order.setTotalOrderAmount(request.getTotalOrderAmount());
        order.setProfit(profit);
        order.setReceivedTime(LocalDateTime.now().minusDays(2));
        order.setTotalRefundAmount(totalRefundAmount);
        order.setStatus(StatusProduct.COMPLETE.getValue());
        order.setOrderCode(generateOrderCode());
        order.setModifyDate(LocalDateTime.now());
        orderRepository.save(order);

        BigDecimal updatedAmount = user.getTotalAmount().subtract(order.getTotalOrderAmount());
        user.setTotalAmount(updatedAmount);
        userRepository.save(user);

        BillOrderResponse billOrderResponse = BillOrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .productImage(order.getProductImage())
                .status(order.getStatus())
                .receivedTime(order.getReceivedTime())
                .totalOrderAmount(order.getTotalOrderAmount())
                .profit(order.getProfit())
                .totalRefundAmount(order.getTotalRefundAmount())
                .interestRate(productModel.getInterestRate())
                .orderCode(order.getOrderCode())
                .build();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, billOrderResponse);
        return response;
    }

    public static String generateOrderCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        String formattedNumber = String.format("%06d", randomNumber);
        return "DH" + formattedNumber;
    }
}
