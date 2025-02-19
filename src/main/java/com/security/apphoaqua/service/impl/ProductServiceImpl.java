package com.security.apphoaqua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.product.CreateProductRequest;
import com.security.apphoaqua.dto.request.product.UpdateProductRequest;
import com.security.apphoaqua.dto.request.product.UploadProductImageRequest;
import com.security.apphoaqua.dto.response.product.ProductListForUserResponse;
import com.security.apphoaqua.dto.response.product.ProductListResponse;
import com.security.apphoaqua.entity.FileStorage;
import com.security.apphoaqua.entity.Order;
import com.security.apphoaqua.entity.Product;
import com.security.apphoaqua.enumeration.StatusProduct;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.FileStorageRepository;
import com.security.apphoaqua.repository.OrderRepository;
import com.security.apphoaqua.repository.ProductRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.service.ProductService;
import com.security.apphoaqua.utils.FileHelperService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.security.apphoaqua.core.response.ResponseStatus.*;
import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    final static String BASE_PATH = "./src";
//    final static String BASE_URL = "http://localhost:8083";
    final static String BASE_URL = "https://xuatkhautraicay.com";
    final static String ORIGINAL = "original/";
    private final ProductRepository productRepository;
    private final FileStorageRepository fileStorageRepository;
    private final FileHelperService fileHelperService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> getAllProduct() {
        var productList = productRepository.findAll();
        var productListResponse = productList.stream().map(product -> ProductListResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .interestRate(product.getInterestRate())
                .imageId(product.getImageId())
                .level(product.getLevel())
                .createDate(product.getCreateDate())
                .build()).collect(Collectors.toCollection(ArrayList::new));
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productListResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> createProduct(CreateProductRequest request) {
        // check if product already exists
        var existsProductName = productRepository.existsByProductName(request.getProductName());
        if (existsProductName) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_NAME_EXIST, errorMapping);
        }
        String productId = UUID.randomUUID().toString().replaceAll("-", "");
        var productsModel = Product.builder()
                .productId(productId)
                .productName(request.getProductName())
                .interestRate(request.getInterestRate())
                .price(request.getPrice())
                .imageId(BASE_URL + "/api/v1/un_auth/products/download/original/" + request.getImageId())
                .level(request.getLevel())
                .createDate(LocalDateTime.now())
                .build();
        productRepository.save(productsModel);
        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> updateProduct(UpdateProductRequest request) {
        var orderList = new ArrayList<Order>();
        var productsModel = productRepository.findById(request.getProductId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });
        if (Objects.nonNull(request.getProductName())) {
            this.validateProductName(request.getProductName(), productsModel.getProductName());
        }
        if (request.getImageId().equalsIgnoreCase(productsModel.getImageId())) {
            productsModel.setImageId(request.getImageId());
        } else {
            productsModel.setImageId(BASE_URL + "/api/v1/un_auth/products/download/original/" + request.getImageId());
            orderRepository.findAllByProductId(request.getProductId()).forEach(order -> {
                order.setProductImage(BASE_URL + "/api/v1/un_auth/products/download/original/" + request.getImageId());
                orderList.add(order);
            });
            orderRepository.saveAll(orderList);
        }
        productsModel.setProductName(request.getProductName());
        productsModel.setPrice(request.getPrice());
        productsModel.setInterestRate(request.getInterestRate());
        productsModel.setLevel(request.getLevel());
        productsModel.setModifyDate(LocalDateTime.now());
        productRepository.save(productsModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productsModel.getProductId());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> deleteProductById(String productId) {
        productRepository.findById(productId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });
        productRepository.deleteById(productId);

        orderRepository.deleteAllByProductIdAndStatus(productId, StatusProduct.WAIT_FOR_COMPLETION.getValue());

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productId);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> uploadFile(UploadProductImageRequest request) {
        var response = new ResponseBody<>();
        var file = request.getFile();
        var rawFileName = request.getFile_name();
        var subDirectory = this.getSubDirectory(request.getFile_directory());
        var fileId = UUID.randomUUID().toString().replaceAll("-", "");
        var currentDateTime = LocalDateTime.now();
        try {
            if (Objects.isNull(rawFileName)) {
                rawFileName = file.getOriginalFilename();
            }
            String directoryPath = BASE_PATH + subDirectory;
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String docTypeId = request.getDoc_type_id();
            var fileName = fileId + "." + fileExtension;
            if (Objects.nonNull(docTypeId)) {
                fileName = docTypeId + "-" + fileName;
            }

            byte[] bytes = new byte[0];
            bytes = file.getBytes();
            if (Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType(),
                    IMAGE_WEBP.getMimeType(), IMAGE_SVG.getMimeType()).contains(file.getContentType())) {
                subDirectory = subDirectory + ORIGINAL;
                String directoryPathOriginal = directoryPath + ORIGINAL;
                this.createDirIfNotExist(directoryPathOriginal);
                Files.write(Paths.get(directoryPathOriginal + fileName), bytes);
            } else {
                createDirIfNotExist(directoryPath);
                Files.write(Paths.get(directoryPath + fileName), bytes);
            }

            FileStorage fileStorageModel = FileStorage.builder()
                    .id(fileId)
                    .fileDirectory(subDirectory)
                    .rawFileName(rawFileName)
                    .fileName(fileName)
                    .fileExtension(fileExtension)
                    .description(request.getDescription())
                    .fileType(request.getType())
                    .createDate(currentDateTime)
                    .build();
            fileStorageRepository.save(fileStorageModel);
            var json = new ObjectMapper().createObjectNode();
            json.putPOJO("imageId", fileId);
            response.setOperationSuccess(SUCCESS, json);
            return response;
        } catch (Exception e) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(UPLOAD_FILE_FAIL.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, UPLOAD_FILE_FAIL, errorMapping);
        }
    }

    @Override
    public byte[] downloadOriginalWithUrl(String fileId) throws IOException {
        FileStorage fileStorageModel = fileStorageRepository.findById(fileId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(FILE_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, FILE_NOT_FOUND, errorMapping);
        });
        String fileDirectory = fileStorageModel.getFileDirectory();

        File file = new File(BASE_PATH + fileDirectory + fileStorageModel.getFileName());
        byte[] bytes = fileHelperService.readAllBytes(file.toPath());
        HttpHeaders headers = getHttpHeaders(fileStorageModel.getRawFileName());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes).getBody();
    }

    @Override
    public ResponseBody<Object> getAllProductsForUser(String userId) {
        var productList = productRepository.findAll();
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        var productListResponse = productList.stream()
                .map(product -> {
                    String status;
                    switch (product.getLevel()) {
                        case 5:
                            status = "active";
                            break;
                        case 3:
                            status = user.isShareholderLevel2() ? "active" : "inactive";
                            break;
                        case 2:
                            status = user.isShareholderLevel3() ? "active" : "inactive";
                            break;
                        case 1:
                            status = user.isShareholderLevel4() ? "active" : "inactive";
                            break;
                        default:
                            status = "inactive";
                            break;
                    }
                    return ProductListForUserResponse.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .interestRate(product.getInterestRate())
                            .imageId(product.getImageId())
                            .level(product.getLevel())
                            .createDate(product.getCreateDate())
                            .status(status)
                            .build();
                })
                .collect(Collectors.toCollection(ArrayList::new));

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productListResponse);
        return response;
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

    public String getSubDirectory(String req) {
        var subDirectory = req;
        if (Objects.isNull(subDirectory)) {
            subDirectory = "";
        } else if (!"/".equals(subDirectory.charAt(subDirectory.length() - 1))) {
            subDirectory = subDirectory + "/";
        }
        return subDirectory;
    }

    private void createDirIfNotExist(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static HttpHeaders getHttpHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }
}
