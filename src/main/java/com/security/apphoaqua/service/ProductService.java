package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.product.CreateProductRequest;

public interface ProductService {
    ResponseBody<Object> getAllProduct();

    ResponseBody<Object> createProduct(CreateProductRequest request);
}
