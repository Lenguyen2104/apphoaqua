package com.security.apphoaqua.controller;

import com.security.apphoaqua.dto.request.product.CreateProductRequest;
import com.security.apphoaqua.dto.request.product.UpdateProductRequest;
import com.security.apphoaqua.dto.request.product.UploadProductImageRequest;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.service.ProductService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Validator validator;

    @GetMapping("/un_auth/products")
    public ResponseEntity<Object> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping("/admin/products/create")
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRequest request) {
//        this.validateRequest(request);
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PostMapping("/admin/products/update")
    public ResponseEntity<Object> updateProduct(@RequestBody UpdateProductRequest request) {
//        this.validateRequest(request);
        return ResponseEntity.ok(productService.updateProduct(request));
    }

    @DeleteMapping("/admin/products/delete/{product_id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("product_id") String productId) {
        return ResponseEntity.ok(productService.deleteProductById(productId));
    }

    @PostMapping(value = "/admin/products/upload_image",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> uploadFileStorage(UploadProductImageRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(productService.uploadFile(request));
    }

    @GetMapping(value = "/un_auth/products/download/original/{imageId}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public ResponseEntity<byte[]> downloadFileOriginalWithUrl(@PathVariable String imageId) throws IOException {
        return ResponseEntity.ok(productService.downloadOriginalWithUrl(imageId));
    }

    @GetMapping("/un_auth/products/{user_id}")
    public ResponseEntity<Object> getAllProductsForUser(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(productService.getAllProductsForUser(userId));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
