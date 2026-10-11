package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.BannerRequest;
import com.skytech.dto.BannerResponse;
import com.skytech.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BannerController {

    private final BannerService service;

    // Public endpoint used by skytech-frontend's HeroBanner component.
    @GetMapping("/slides")
    public List<BannerResponse> getActiveSlides() {
        return service.getActive();
    }

    @GetMapping("/api/banner")
    public ResponseEntity<ResponseObject<List<BannerResponse>>> getAll() {
        ResponseObject<List<BannerResponse>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/banner/{id}")
    public ResponseEntity<ResponseObject<BannerResponse>> getById(@PathVariable Long id) {
        ResponseObject<BannerResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/banner")
    public ResponseEntity<ResponseObject<BannerResponse>> create(@RequestBody BannerRequest request) {
        ResponseObject<BannerResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/banner/{id}")
    public ResponseEntity<ResponseObject<BannerResponse>> update(@PathVariable Long id, @RequestBody BannerRequest request) {
        ResponseObject<BannerResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.update(id, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/banner/{id}")
    public ResponseEntity<ResponseObject<Boolean>> delete(@PathVariable Long id) {
        ResponseObject<Boolean> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.delete(id));
        return ResponseEntity.ok(response);
    }
}
