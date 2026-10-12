package com.skytech.dto;

import com.skytech.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {
    private String name;
    private String sku;
    private String model;
    private Long categoryId;
    private Long brandId;
    private String description;
    private String featuredImageUrl;
    private ProductStatus status;

    private ProductPricingDto pricing;
    private ProductInventoryDto inventory;
    private List<ProductSpecificationDto> specifications = new ArrayList<>();
    private List<String> galleryImages = new ArrayList<>();
}
