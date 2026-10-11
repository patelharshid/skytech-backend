package com.skytech.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerRequest {
    private String title;
    private String subtitle;
    private String image;
    private List<String> bullets;
    private Integer displayOrder;
    private Boolean isActive;
    private Integer createdBy;
    private Integer updatedBy;
}
