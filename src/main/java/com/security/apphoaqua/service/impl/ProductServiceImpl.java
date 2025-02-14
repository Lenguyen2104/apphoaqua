package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.product.CreateProductRequest;
import com.security.apphoaqua.dto.response.product.ProductListResponse;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.ProductRepository;
import com.security.apphoaqua.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.security.apphoaqua.core.response.ResponseStatus.PRODUCT_NAME_EXIST;
import static com.security.apphoaqua.core.response.ResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ResponseBody<Object> getAllProduct() {
        var productList = productRepository.findAll();
        var productListResponse = productList.stream().map(product -> {
            var productResponse = new ProductListResponse();
            productResponse.setProductId(productResponse.getProductId());
            productResponse.setName(product.getProductName());
            productResponse.setPrice(product.getPrice());
            return productResponse;
        }).collect(Collectors.toList());
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productListResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> createProduct(CreateProductRequest request) {
        // check if product already exists
        validateProductName(request.getProductName(), null);
        return null;
    }

    private void validateProductName(String productName, String productNamePresent) {
        var existsProductName = productRepository.existsByProductName(productName);
        if (!Objects.equals(productName, productNamePresent) && existsProductName) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_NAME_EXIST, errorMapping);
        }
    }
}
