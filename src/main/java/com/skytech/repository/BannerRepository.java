package com.skytech.repository;

import com.skytech.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByDisplayOrderAsc();
    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
}
