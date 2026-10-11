package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.UserRequest;
import com.skytech.dto.auth.UserResponse;
import com.skytech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<ResponseObject<List<UserResponse>>> getAll() {
        ResponseObject<List<UserResponse>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<UserResponse>> getById(@PathVariable("id") Long id) {
        ResponseObject<UserResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<UserResponse>> create(@RequestBody UserRequest request) {
        ResponseObject<UserResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<UserResponse>> update(@PathVariable("id") Long id, @RequestBody UserRequest request) {
        ResponseObject<UserResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.update(id, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Boolean>> delete(@PathVariable("id") Long id) {
        ResponseObject<Boolean> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.delete(id));
        return ResponseEntity.ok(response);
    }
}
