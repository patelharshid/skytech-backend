package com.skytech.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDto {
    private String specGroup = "General";
    private String specKey;
    private String specValue;
    private Integer displayOrder = 0;
}
