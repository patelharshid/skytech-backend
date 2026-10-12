package com.skytech.service;

import com.skytech.dto.BrandRequest;
import com.skytech.entity.Brand;
import com.skytech.error.CustomException;
import com.skytech.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository repository;

    public List<Brand> getAll(Boolean activeOnly) {
        if (activeOnly != null && activeOnly) {
            return repository.findByIsActiveTrueOrderByNameAsc();
        }
        return repository.findAllByOrderByNameAsc();
    }

    public Brand getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Brand not found with id: " + id));
    }

    @Transactional
    public Brand create(BrandRequest request) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CustomException("name", "Brand name cannot be blank");
        }
        String trimmedName = request.getName().trim();
        if (repository.findByNameIgnoreCase(trimmedName).isPresent()) {
            throw new CustomException("name", "Brand already exists with name: " + trimmedName);
        }

        Brand brand = new Brand();
        brand.setName(trimmedName);
        brand.setLogoUrl(request.getLogoUrl() != null ? request.getLogoUrl().trim() : null);
        brand.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return repository.save(brand);
    }

    @Transactional
    public Brand update(Long id, BrandRequest request) {
        Brand existing = getById(id);

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            String trimmedName = request.getName().trim();
            repository.findByNameIgnoreCase(trimmedName).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new CustomException("name", "Another brand already exists with name: " + trimmedName);
                }
            });
            existing.setName(trimmedName);
        }

        if (request.getLogoUrl() != null) {
            existing.setLogoUrl(request.getLogoUrl().trim());
        }

        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }

        return repository.save(existing);
    }

    @Transactional
    public Boolean delete(Long id) {
        Brand existing = getById(id);
        repository.delete(existing);
        return true;
    }
}
