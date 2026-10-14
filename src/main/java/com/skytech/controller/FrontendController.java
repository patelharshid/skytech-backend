package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.BannerResponse;
import com.skytech.entity.TopMarquee;
import com.skytech.service.FrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("front-end")
public class FrontendController {

    @Autowired
    FrontendService frontendService;

    @GetMapping("getAllTopMarquee")
    public ResponseEntity<ResponseObject<List<TopMarquee>>> getAllTopMarquee() {
        ResponseObject<List<TopMarquee>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(frontendService.getAllTopMarquee());
        return ResponseEntity.ok(response);
    }

    @GetMapping("getAllBanner")
    public ResponseEntity<ResponseObject<List<BannerResponse>>> getAllBanner() {
        ResponseObject<List<BannerResponse>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(frontendService.getAllBanner());
        return ResponseEntity.ok(response);
    }

}
