package com.skytech.dto;

import com.skytech.entity.DiscountType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPricingDto {
    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;
    private DiscountType discountType = DiscountType.NONE;
    private BigDecimal discountValue = BigDecimal.ZERO;
    private BigDecimal finalPrice;
    private BigDecimal discountPercentage;
}
