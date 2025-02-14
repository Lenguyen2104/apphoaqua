package com.security.apphoaqua.dto.request.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @NotBlank(message = "Giá sản phẩm không được để trống")
    private String price;

    @NotBlank(message = "Lãi suất sản phẩm không được để trống")
    private String interestRate;

    @NotBlank(message = "Ảnh sản phẩm không được để trống")
    private String imageId;

    @NotBlank(message = "Cổ đông sản phẩm không được để trống")
    private String level;
}

