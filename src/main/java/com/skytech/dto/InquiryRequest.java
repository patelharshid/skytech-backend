package com.skytech.dto;

import com.skytech.entity.InquiryStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRequest {
    private String fullName;
    private String phone;
    private String email;
    private String city;
    private String message;
    private InquiryStatus status;
}
