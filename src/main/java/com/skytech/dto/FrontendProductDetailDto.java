package com.skytech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrontendProductDetailDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String category;
    private String brand;
    private String model;
    private String sku;
    private String condition;

    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;
    private BigDecimal discountPercentage;

    private String image;
    private List<String> images = new ArrayList<>();

    private String processor;
    private String storage;
    private String ram;
    private String screen;
    private String graphic;
    private String os;
    private String connection;

    private List<String> specs = new ArrayList<>();

    private Boolean inStock;
    private Integer quantity;
}
