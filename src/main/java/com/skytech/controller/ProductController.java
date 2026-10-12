package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.ProductCreateRequest;
import com.skytech.dto.ProductResponse;
import com.skytech.dto.ProductUpdateRequest;
import com.skytech.dto.StockUpdateRequest;
import com.skytech.entity.ProductStatus;
import com.skytech.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/product" })
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<ResponseObject<List<ProductResponse>>> getAll(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "brandId", required = false) Long brandId,
            @RequestParam(name = "status", required = false) ProductStatus status) {
        ResponseObject<List<ProductResponse>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll(search, categoryId, brandId, status));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<ProductResponse>> getById(@PathVariable("id") Long id) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ResponseObject<ProductResponse>> getBySku(@PathVariable("sku") String sku) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getBySku(sku));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<ProductResponse>> create(@RequestBody ProductCreateRequest request) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<ProductResponse>> update(
            @PathVariable("id") Long id,
            @RequestBody ProductUpdateRequest request) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.update(id, request));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ResponseObject<ProductResponse>> updateStock(
            @PathVariable("id") Long id,
            @RequestBody StockUpdateRequest request) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.updateStock(id, request));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseObject<ProductResponse>> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") ProductStatus status) {
        ResponseObject<ProductResponse> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.updateStatus(id, status));
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
