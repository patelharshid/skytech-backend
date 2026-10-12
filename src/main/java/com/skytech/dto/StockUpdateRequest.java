package com.skytech.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {
    private Integer newQuantity;
    private Integer quantityDelta;
    private Integer lowStockThreshold;
}
