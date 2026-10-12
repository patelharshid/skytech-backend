package com.skytech.service;

import com.skytech.dto.CategoryRequest;
import com.skytech.entity.Category;
import com.skytech.error.CustomException;
import com.skytech.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> getAll() {
        return repository.findAllByOrderByIdAsc();
    }

    public Category getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Category record not found with id: " + id));
    }

    public Category create(CategoryRequest request) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CustomException("name", "Category name cannot be blank");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setImageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null);

        return repository.save(category);
    }

    public Category update(Long id, CategoryRequest request) {
        Category existing = getById(id);

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            existing.setName(request.getName().trim());
        }

        if (request.getImageUrl() != null) {
            existing.setImageUrl(request.getImageUrl().trim());
        }

        return repository.save(existing);
    }

    public Boolean delete(Long id) {
        Category existing = getById(id);
        repository.delete(existing);
        return true;
    }
}
