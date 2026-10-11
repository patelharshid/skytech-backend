package com.skytech.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String session;
    private String refresh;
    private UserResponse user;
}
