package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.BrandRequest;
import com.skytech.entity.Brand;
import com.skytech.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/brand" })
@RequiredArgsConstructor
public class BrandController {

    private final BrandService service;

    @GetMapping
    public ResponseEntity<ResponseObject<List<Brand>>> getAll(
            @RequestParam(name = "activeOnly", required = false) Boolean activeOnly) {
        ResponseObject<List<Brand>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll(activeOnly));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Brand>> getById(@PathVariable("id") Long id) {
        ResponseObject<Brand> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<Brand>> create(@RequestBody BrandRequest request) {
        ResponseObject<Brand> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Brand>> update(
            @PathVariable("id") Long id,
            @RequestBody BrandRequest request) {
        ResponseObject<Brand> response = new ResponseObject<>();
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
