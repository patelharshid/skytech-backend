package com.skytech.service;

import com.skytech.dto.InquiryRequest;
import com.skytech.entity.Inquiry;
import com.skytech.entity.InquiryStatus;
import com.skytech.error.CustomException;
import com.skytech.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    public List<Inquiry> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public List<Inquiry> getByStatus(InquiryStatus status) {
        if (status == null) {
            return getAll();
        }
        return repository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Inquiry getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Inquiry not found with id: " + id));
    }

    public Inquiry create(InquiryRequest request) {
        if (request == null) {
            throw new CustomException("request", "Inquiry request cannot be null");
        }
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new CustomException("fullName", "Full name is required");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new CustomException("phone", "Phone number is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new CustomException("email", "Email address is required");
        }
        if (request.getCity() == null || request.getCity().trim().isEmpty()) {
            throw new CustomException("city", "City is required");
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setFullName(request.getFullName().trim());
        inquiry.setPhone(request.getPhone().trim());
        inquiry.setEmail(request.getEmail().trim());
        inquiry.setCity(request.getCity().trim());
        inquiry.setMessage(request.getMessage() != null ? request.getMessage().trim() : null);
        inquiry.setStatus(request.getStatus() != null ? request.getStatus() : InquiryStatus.NEW);

        return repository.save(inquiry);
    }

    public Inquiry updateStatus(Long id, InquiryStatus status) {
        if (status == null) {
            throw new CustomException("status", "Status cannot be null");
        }

        Inquiry existing = getById(id);
        existing.setStatus(status);
        return repository.save(existing);
    }

    public Boolean delete(Long id) {
        Inquiry existing = getById(id);
        repository.delete(existing);
        return true;
    }
}
