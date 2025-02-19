package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.product.CreateProductRequest;
import com.security.apphoaqua.dto.request.product.UpdateProductRequest;
import com.security.apphoaqua.dto.request.product.UploadProductImageRequest;

import java.io.IOException;

public interface ProductService {
    ResponseBody<Object> getAllProduct();

    ResponseBody<Object> createProduct(CreateProductRequest request);

    ResponseBody<Object> updateProduct(UpdateProductRequest request);

    ResponseBody<Object> deleteProductById(String productId);

    ResponseBody<Object> uploadFile(UploadProductImageRequest request);

    byte[] downloadOriginalWithUrl(String imageId) throws IOException;

    ResponseBody<Object> getAllProductsForUser(String userId);
}
