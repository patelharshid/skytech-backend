package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.TopMarqueeRequest;
import com.skytech.entity.TopMarquee;
import com.skytech.service.TopMarqueeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topmarquee")
@RequiredArgsConstructor
public class TopMarqueeController {

    private final TopMarqueeService service;

    @GetMapping("getTopMarquee")
    public ResponseEntity<ResponseObject<List<TopMarquee>>> getAll() {
        ResponseObject<List<TopMarquee>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<TopMarquee>> getById(@PathVariable("id") Long id) {
        ResponseObject<TopMarquee> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<TopMarquee>> create(@RequestBody TopMarqueeRequest request) {
        ResponseObject<TopMarquee> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<TopMarquee>> update(@PathVariable("id") Long id, @RequestBody TopMarqueeRequest request) {
        ResponseObject<TopMarquee> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.update(id, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<TopMarquee>> setActive(
            @PathVariable("id") Long id,
            @RequestBody TopMarqueeRequest request) {
        ResponseObject<TopMarquee> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.setActive(id, request.getIsActive()));
        return ResponseEntity.ok(response);
    }
}
