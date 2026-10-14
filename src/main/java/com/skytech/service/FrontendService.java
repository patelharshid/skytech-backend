package com.skytech.service;

import com.skytech.dto.BannerResponse;
import com.skytech.entity.TopMarquee;
import com.skytech.repository.TopMarqueeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontendService {
    @Autowired
    TopMarqueeRepository topMarqueeRepository;

    @Autowired
    BannerService bannerService;

    public List<TopMarquee> getAllTopMarquee() {
        return topMarqueeRepository.findByIsActiveTrueOrderByCreatedDateTimeDesc();
    }

    public List<BannerResponse> getAllBanner() {
        return bannerService.getActive();
    }
}
