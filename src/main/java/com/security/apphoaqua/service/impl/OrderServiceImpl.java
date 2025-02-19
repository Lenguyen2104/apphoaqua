package com.security.apphoaqua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.order.AllOrderRequest;
import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.dto.request.order.OrderSearchRequest;
import com.security.apphoaqua.dto.response.order.*;
import com.security.apphoaqua.entity.Order;
import com.security.apphoaqua.entity.User;
import com.security.apphoaqua.enumeration.StatusProduct;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.OrderRepository;
import com.security.apphoaqua.repository.ProductRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.security.apphoaqua.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private static final String DEFAULT_SORT_FIELD = "createDate";

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
        if (user.getTotalAmount().compareTo(request.getTotalOrderAmount()) < 0) {
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
        order.setReceivedTime(LocalDateTime.now().plusDays(2));
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

    @Override
    public ResponseBody<Object> todayOrder(String userId) {
        var userModel = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        List<Order> ordersComplete = orderRepository.findAllByUserIdAndStatus(userId, StatusProduct.COMPLETE.getValue());
        List<Order> ordersWaitForCompletion = orderRepository.findAllByUserIdAndStatus(userId, StatusProduct.WAIT_FOR_COMPLETION.getValue());
        BigDecimal profitToday = ordersWaitForCompletion.stream()
                .filter(order -> order.getCreateDate().toLocalDate().equals(LocalDate.now()))
                .map(Order::getProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        BigDecimal pendingAmount = ordersWaitForCompletion.stream().map(Order::getTotalOrderAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        TodayOrderResponse todayOrderResponse = TodayOrderResponse.builder()
                .accountBalance(userModel.getTotalAmount())
                .profitToday(profitToday)
                .numberOfCompleteApplications(ordersComplete.size())
                .pendingAmount(pendingAmount)
                .build();
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, todayOrderResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> allOrders(AllOrderRequest request) {
        List<AllOrderResponse> orderResponse;
        if (request.getStatus() == null) {
            List<Order> orders = orderRepository.findAllByUserId(request.getUserId());
            orderResponse = orders.stream().map(order -> AllOrderResponse.builder()
                    .orderId(order.getOrderId())
                    .userId(order.getUserId())
                    .productId(order.getProductId())
                    .productImage(order.getProductImage())
                    .status(order.getStatus())
                    .totalOrderAmount(order.getTotalOrderAmount())
                    .profit(order.getProfit())
                    .totalRefundAmount(order.getTotalRefundAmount())
                    .interestRate(countInterestRate(order.getTotalOrderAmount(), order.getProfit()))
                    .productName(productRepository.findById(order.getProductId()).get().getProductName())
                    .build()).toList();
        } else {
            List<Order> orders = orderRepository.findAllByUserIdAndStatus(request.getUserId(), request.getStatus());
            orderResponse = orders.stream().map(order -> AllOrderResponse.builder()
                    .orderId(order.getOrderId())
                    .userId(order.getUserId())
                    .productId(order.getProductId())
                    .productImage(order.getProductImage())
                    .status(order.getStatus())
                    .totalOrderAmount(order.getTotalOrderAmount())
                    .profit(order.getProfit())
                    .totalRefundAmount(order.getTotalRefundAmount())
                    .interestRate(countInterestRate(order.getTotalOrderAmount(), order.getProfit()))
                    .productName(productRepository.findById(order.getProductId()).get().getProductName())
                    .build()).toList();
        }
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, orderResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> getAllOrders(OrderSearchRequest request) {
        var mapper = new ObjectMapper();
        var json = mapper.createObjectNode();

        Pageable pageable;

        if (request.getSortBy() == null || request.getSortBy().isEmpty()) {
            request.setSortBy(DEFAULT_SORT_FIELD);
        }

        if (request.getSortDirection() == null || request.getSortDirection().isEmpty()) {
            request.setSortDirection("asc");
        }

        if (request.getSortDirection().equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).descending());
        } else {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).ascending());
        }

        Page<Order> listOrderPage = orderRepository.findAllOrder(pageable);

        var orders = listOrderPage.getContent();
        List<String> userIds = orders.stream().map(Order::getUserId).toList();

        List<User> users = userRepository.findByIdIn(userIds);
        Map<String, String> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, User::getFullName));

        var orderResponse = orders.stream().map(order ->
                OrderSearchResponse.builder()
                        .userName(userMap.get(order.getUserId()))
                        .orderCode(order.getOrderCode() == null ? "Đợi đặt hàng" : order.getOrderCode())
                        .createdDate(order.getCreateDate().toString())
                        .status(order.getStatus())
                        .totalOrderAmount(order.getTotalOrderAmount())
                        .profit(order.getProfit())
                        .build());

        json.putPOJO("page_number", request.getPageNumber());
        json.putPOJO("total_records", listOrderPage.getTotalElements());
        json.putPOJO("page_size", request.getPageSize());
        json.putPOJO("list_order", orderResponse);
        json.putPOJO("total_page", listOrderPage.getTotalPages());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    public static String generateOrderCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        String formattedNumber = String.format("%06d", randomNumber);
        return "DH" + formattedNumber;
    }

    private Double countInterestRate(BigDecimal totalOrderAmount, BigDecimal profit) {
        Double interestRate = 0.0;
        if (totalOrderAmount != null && totalOrderAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = profit.divide(totalOrderAmount, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            interestRate = rate.doubleValue();
        }
        return interestRate;
    }
}
