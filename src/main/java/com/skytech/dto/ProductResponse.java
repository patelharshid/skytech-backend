package com.skytech.dto;

import com.skytech.entity.Category;
import com.skytech.entity.Brand;
import com.skytech.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String sku;
    private String model;
    private String description;
    private ProductStatus status;
    private String featuredImageUrl;

    private Category category;
    private Brand brand;

    private ProductPricingDto pricing;
    private ProductInventoryDto inventory;
    private List<ProductSpecificationDto> specifications = new ArrayList<>();
    private List<String> galleryImages = new ArrayList<>();

    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
