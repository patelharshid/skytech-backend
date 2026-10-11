package com.skytech.service;

import com.skytech.dto.BannerRequest;
import com.skytech.dto.BannerResponse;
import com.skytech.entity.Banner;
import com.skytech.error.CustomException;
import com.skytech.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository repository;

    public List<BannerResponse> getAll() {
        return repository.findAllByOrderByDisplayOrderAsc().stream().map(this::toResponse).toList();
    }

    public List<BannerResponse> getActive() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc().stream().map(this::toResponse).toList();
    }

    public BannerResponse getById(Long id) {
        return toResponse(findBanner(id));
    }

    public BannerResponse create(BannerRequest request) {
        validate(request);
        LocalDateTime now = LocalDateTime.now();
        Banner banner = new Banner();
        banner.setTitle(request.getTitle().trim());
        banner.setSubtitle(request.getSubtitle());
        banner.setImage(request.getImage().trim());
        banner.setBullets(request.getBullets() == null ? new ArrayList<>() : new ArrayList<>(request.getBullets()));
        banner.setDisplayOrder(request.getDisplayOrder() == null ? 0 : request.getDisplayOrder());
        banner.setIsActive(request.getIsActive() == null || request.getIsActive());
        banner.setCreatedBy(request.getCreatedBy() == null ? 1 : request.getCreatedBy());
        banner.setUpdatedBy(request.getUpdatedBy() == null ? 1 : request.getUpdatedBy());
        banner.setCreatedAt(now);
        banner.setUpdatedAt(now);
        return toResponse(repository.save(banner));
    }

    public BannerResponse update(Long id, BannerRequest request) {
        Banner banner = findBanner(id);
        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) throw new CustomException("title", "Title cannot be blank");
            banner.setTitle(request.getTitle().trim());
        }
        if (request.getSubtitle() != null) banner.setSubtitle(request.getSubtitle());
        if (request.getImage() != null) {
            if (request.getImage().isBlank()) throw new CustomException("image", "Image is required");
            banner.setImage(request.getImage().trim());
        }
        if (request.getBullets() != null) banner.setBullets(new ArrayList<>(request.getBullets()));
        if (request.getDisplayOrder() != null) banner.setDisplayOrder(request.getDisplayOrder());
        if (request.getIsActive() != null) banner.setIsActive(request.getIsActive());
        if (request.getUpdatedBy() != null) banner.setUpdatedBy(request.getUpdatedBy());
        banner.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(banner));
    }

    public Boolean delete(Long id) {
        repository.delete(findBanner(id));
        return true;
    }

    private Banner findBanner(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException("id", "Banner not found with id: " + id));
    }

    private void validate(BannerRequest request) {
        if (request == null || request.getTitle() == null || request.getTitle().isBlank()) {
            throw new CustomException("title", "Title cannot be blank");
        }
        if (request.getImage() == null || request.getImage().isBlank()) {
            throw new CustomException("image", "Image is required");
        }
    }

    private BannerResponse toResponse(Banner banner) {
        return new BannerResponse(banner.getId(), banner.getTitle(), banner.getSubtitle(), banner.getImage(),
                banner.getBullets(), banner.getDisplayOrder(), banner.getIsActive());
    }
}
