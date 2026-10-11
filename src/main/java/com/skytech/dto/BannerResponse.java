package com.skytech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BannerResponse {
    private Long id;
    private String title;
    private String subtitle;
    private String image;
    private List<String> bullets;
    private Integer displayOrder;
    private Boolean isActive;
}
