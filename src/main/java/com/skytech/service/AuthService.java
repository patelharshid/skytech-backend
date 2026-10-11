package com.skytech.service;

import com.skytech.dto.auth.LoginRequest;
import com.skytech.dto.auth.LoginResponse;
import com.skytech.entity.User;
import com.skytech.error.CustomException;
import com.skytech.repository.UserRepository;
import com.skytech.configuration.JwtHelper;
import com.skytech.helper.CryptoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            throw new CustomException("credentials", "Email and password are required");
        }
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new CustomException("credentials", "Invalid email or password"));
        if (!Boolean.TRUE.equals(user.getIsactive())) {
            throw new CustomException("user", "Account is inactive. Please contact admin");
        }
        if (!StringUtils.hasText(user.getHashkey()) || !CryptoHelper.encryptPassword(request.getPassword(), user.getHashkey()).equals(user.getPassword())) {
            throw new CustomException("credentials", "Invalid email or password");
        }

        LoginResponse response = new LoginResponse();
        response.setSession(jwtHelper.generateAccessToken(user));
        response.setRefresh(jwtHelper.generateRefreshToken(user));
        response.setUser(new com.skytech.dto.auth.UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getMobileno()));
        return response;
    }
}
