package com.skytech.service;

import com.skytech.dto.UserRequest;
import com.skytech.dto.auth.UserResponse;
import com.skytech.entity.User;
import com.skytech.error.CustomException;
import com.skytech.helper.CryptoHelper;
import com.skytech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<UserResponse> getAll() {
        return repository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getMobileno()))
                .toList();
    }

    public UserResponse getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "User not found with id: " + id));
        return new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getMobileno());
    }

    public UserResponse create(UserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new CustomException("email", "Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new CustomException("password", "Password is required");
        }
        if (repository.findByEmailIgnoreCase(request.getEmail().trim()).isPresent()) {
            throw new CustomException("email", "A user with this email already exists");
        }

        String hashKey = CryptoHelper.getHashKey();
        User user = new User();
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setMobileno(request.getMobileNo());
        user.setEmail(request.getEmail().trim());
        user.setHashkey(hashKey);
        user.setPassword(CryptoHelper.encryptPassword(request.getPassword(), hashKey));
        user.setIsactive(request.getIsActive() == null || request.getIsActive());
        user.setCreateby(request.getCreatedBy() == null ? 1 : request.getCreatedBy());
        user.setCreateddatetime(LocalDateTime.now());
        user.setUpdatedatetime(LocalDateTime.now());
        user = repository.save(user);
        return new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getMobileno());
    }

    public UserResponse update(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "User not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            User existing = repository.findByEmailIgnoreCase(request.getEmail().trim()).orElse(null);
            if (existing != null && !existing.getId().equals(id)) {
                throw new CustomException("email", "A user with this email already exists");
            }
            user.setEmail(request.getEmail().trim());
        }
        if (request.getFirstName() != null) user.setFirstname(request.getFirstName());
        if (request.getLastName() != null) user.setLastname(request.getLastName());
        if (request.getMobileNo() != null) user.setMobileno(request.getMobileNo());
        if (request.getIsActive() != null) user.setIsactive(request.getIsActive());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String hashKey = CryptoHelper.getHashKey();
            user.setHashkey(hashKey);
            user.setPassword(CryptoHelper.encryptPassword(request.getPassword(), hashKey));
        }
        user.setUpdatedatetime(LocalDateTime.now());
        user = repository.save(user);
        return new UserResponse(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getMobileno());
    }

    public Boolean delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "User not found with id: " + id));
        repository.delete(user);
        return true;
    }
}
