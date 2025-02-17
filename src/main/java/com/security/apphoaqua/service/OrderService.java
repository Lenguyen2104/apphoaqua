package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.order.BillOrderRequest;
import com.security.apphoaqua.dto.request.order.CreateOrderRequest;

public interface OrderService {
    ResponseBody<Object> createOrder(CreateOrderRequest request);

    ResponseBody<Object> billOrder(BillOrderRequest request);
}
