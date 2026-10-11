package com.skytech.service;

import com.skytech.dto.TopMarqueeRequest;
import com.skytech.entity.TopMarquee;
import com.skytech.error.CustomException;
import com.skytech.repository.TopMarqueeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopMarqueeService {

    private final TopMarqueeRepository repository;

    public List<TopMarquee> getAll() {
        return repository.findByIsActiveTrueOrderByCreatedDateTimeDesc();
    }

    public TopMarquee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "TopMarquee record not found with id: " + id));
    }

    public TopMarquee create(TopMarqueeRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new CustomException("title", "Title cannot be blank");
        }

        TopMarquee marquee = TopMarquee.builder()
                .title(request.getTitle().trim())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .createdBy(request.getCreatedBy() != null ? request.getCreatedBy() : 1)
                .updatedBy(request.getUpdatedBy() != null ? request.getUpdatedBy() : 1)
                .build();

        return repository.save(marquee);
    }

    public TopMarquee update(Long id, TopMarqueeRequest request) {
        TopMarquee existing = getById(id);

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            existing.setTitle(request.getTitle().trim());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }
        if (request.getUpdatedBy() != null) {
            existing.setUpdatedBy(request.getUpdatedBy());
        } else {
            existing.setUpdatedBy(1);
        }

        return repository.save(existing);
    }

    public Boolean delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomException("id", "TopMarquee record not found with id: " + id);
        }
        repository.deleteById(id);
        return true;
    }
}
