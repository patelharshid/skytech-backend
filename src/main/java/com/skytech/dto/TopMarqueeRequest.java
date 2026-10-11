package com.skytech.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopMarqueeRequest {
    private String title;
    private Boolean isActive;
    private Integer createdBy;
    private Integer updatedBy;
}
