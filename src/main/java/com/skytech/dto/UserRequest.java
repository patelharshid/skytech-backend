package com.skytech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String email;
    private String password;
    private Boolean isActive;
    private Integer createdBy;
}
