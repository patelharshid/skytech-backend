package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.auth.LoginRequest;
import com.skytech.dto.auth.LoginResponse;
import com.skytech.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/auth", "/api/auth"})
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject<LoginResponse>> login(@RequestBody LoginRequest request) {
        ResponseObject<LoginResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(authService.login(request));
        return ResponseEntity.ok(response);
    }
}
