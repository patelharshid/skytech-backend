package com.skytech.repository;

import com.skytech.entity.TopMarquee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopMarqueeRepository extends JpaRepository<TopMarquee, Long> {

    List<TopMarquee> findByIsActiveTrueOrderByCreatedDateTimeDesc();
}
