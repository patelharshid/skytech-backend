package com.skytech.service;

import com.skytech.dto.BannerRequest;
import com.skytech.dto.BannerResponse;
import com.skytech.configuration.AwsConfiguration;
import com.skytech.entity.Banner;
import com.skytech.error.CustomException;
import com.skytech.repository.BannerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.skytech.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository repository;
    private final AwsConfiguration awsConfiguration;

    public List<BannerResponse> getAll() {
        return repository.findAllByOrderByDisplayOrderAsc().stream().map(this::toResponse).toList();
    }

    public List<BannerResponse> getActive() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc().stream().map(this::toResponse).toList();
    }

    public BannerResponse getById(Long id) {
        return toResponse(findBanner(id));
    }

    @Transactional
    public BannerResponse create(BannerRequest request, MultipartFile image) {
        validate(request);
        LocalDateTime now = LocalDateTime.now();
        Banner banner = new Banner();
        banner.setTitle(request.getTitle().trim());
        banner.setSubtitle(request.getSubtitle());
        banner.setImage(uploadImage(image));
        banner.setBullets(request.getBullets() == null ? new ArrayList<>() : new ArrayList<>(request.getBullets()));
        banner.setDisplayOrder(request.getDisplayOrder() == null ? 0 : request.getDisplayOrder());
        banner.setIsActive(request.getIsActive() == null || request.getIsActive());
        banner.setCreatedBy(request.getCreatedBy() == null ? 1 : request.getCreatedBy());
        banner.setUpdatedBy(request.getUpdatedBy() == null ? 1 : request.getUpdatedBy());
        banner.setCreatedAt(now);
        banner.setUpdatedAt(now);
        return toResponse(repository.save(banner));
    }

    @Transactional
    public BannerResponse update(Long id, BannerRequest request, MultipartFile image) {
        Banner banner = findBanner(id);
        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) throw new CustomException("title", "Title cannot be blank");
            banner.setTitle(request.getTitle().trim());
        }
        if (request.getSubtitle() != null) banner.setSubtitle(request.getSubtitle());
        if (request.getBullets() != null) banner.setBullets(new ArrayList<>(request.getBullets()));
        if (request.getDisplayOrder() != null) banner.setDisplayOrder(request.getDisplayOrder());
        if (request.getIsActive() != null) banner.setIsActive(request.getIsActive());
        if (request.getUpdatedBy() != null) banner.setUpdatedBy(request.getUpdatedBy());
        banner.setUpdatedAt(LocalDateTime.now());
        String oldImage = banner.getImage();
        if (image != null && !image.isEmpty()) {
            banner.setImage(uploadImage(image));
        }
        Banner saved = repository.save(banner);
        if (!oldImage.equals(saved.getImage())) awsConfiguration.deleteFile(oldImage);
        return toResponse(saved);
    }

    @Transactional
    public Boolean delete(Long id) {
        Banner banner = findBanner(id);
        repository.delete(banner);
        awsConfiguration.deleteFile(banner.getImage());
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
    }

    private BannerResponse toResponse(Banner banner) {
        return new BannerResponse(banner.getId(), banner.getTitle(), banner.getSubtitle(), awsConfiguration.getPresignedUrl(banner.getImage()),
                banner.getBullets(), banner.getDisplayOrder(), banner.getIsActive());
    }

    private String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) throw new CustomException("image", "Image file is required");
        String contentType = image.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new CustomException("image", "Uploaded file must be an image");
        }

        String key = "skytech/banners/" + FileHandler.generateUniqueS3Key(image);
        File temporaryFile = FileHandler.convertMultipartFileToFile(image);
        try {
            awsConfiguration.uploadFile(key, temporaryFile, contentType);
            return key;
        } finally {
            try {
                Files.deleteIfExists(temporaryFile.toPath());
            } catch (IOException ignored) {
                // The S3 upload has completed; failed temporary-file cleanup must not undo it.
            }
        }
    }
}
