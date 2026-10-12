package com.skytech.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventoryDto {
    private Integer quantity = 0;
    private Integer lowStockThreshold = 5;
    private Boolean isInStock = false;
    private Boolean isLowStock = false;
    private LocalDateTime lastRestockedAt;
}
