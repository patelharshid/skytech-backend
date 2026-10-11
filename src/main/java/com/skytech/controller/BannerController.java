package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.BannerRequest;
import com.skytech.dto.BannerResponse;
import com.skytech.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/api/banner", consumes = "multipart/form-data")
    public ResponseEntity<ResponseObject<BannerResponse>> create(
            @RequestPart("data") BannerRequest request,
            @RequestPart("image") MultipartFile image) {
        ResponseObject<BannerResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request, image));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/api/banner/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseObject<BannerResponse>> update(
            @PathVariable Long id,
            @RequestPart("data") BannerRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        ResponseObject<BannerResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.update(id, request, image));
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
