package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.InquiryRequest;
import com.skytech.entity.Inquiry;
import com.skytech.entity.InquiryStatus;
import com.skytech.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/inquiry" })
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService service;

    @GetMapping
    public ResponseEntity<ResponseObject<List<Inquiry>>> getAll(
            @RequestParam(value = "status", required = false) InquiryStatus status) {
        ResponseObject<List<Inquiry>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(status != null ? service.getByStatus(status) : service.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Inquiry>> getById(@PathVariable("id") Long id) {
        ResponseObject<Inquiry> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.getById(id));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseObject<Inquiry>> create(@RequestBody InquiryRequest request) {
        ResponseObject<Inquiry> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(service.create(request));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseObject<Inquiry>> updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") InquiryStatus status) {
        ResponseObject<Inquiry> response = new ResponseObject<>();
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
