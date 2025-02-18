package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.order.AllOrderRequest;
import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;
import com.security.apphoaqua.dto.request.order.OrderSearchRequest;

public interface OrderService {
    ResponseBody<Object> createOrder(CreateOrderRequest request);

    ResponseBody<Object> billOrder(BillOrderRequest request);

    ResponseBody<Object> todayOrder(String userId);

    ResponseBody<Object> allOrders(AllOrderRequest request);

    ResponseBody<Object> getAllOrders(OrderSearchRequest request);
}
