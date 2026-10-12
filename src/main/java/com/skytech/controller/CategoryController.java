package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.CategoryRequest;
import com.skytech.entity.Category;
import com.skytech.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/category" })
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<ResponseObject<List<Category>>> getAll() {
        ResponseObject<List<Category>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Category>> getById(@PathVariable("id") Long id) {
        ResponseObject<Category> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<Category>> create(@RequestBody CategoryRequest request) {
        ResponseObject<Category> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Category>> update(
            @PathVariable("id") Long id,
            @RequestBody CategoryRequest request) {
        ResponseObject<Category> response = new ResponseObject<>();
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
