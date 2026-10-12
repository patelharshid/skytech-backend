package com.skytech.repository;

import com.skytech.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findAllByOrderByNameAsc();

    List<Brand> findByIsActiveTrueOrderByNameAsc();

    Optional<Brand> findByNameIgnoreCase(String name);
}
