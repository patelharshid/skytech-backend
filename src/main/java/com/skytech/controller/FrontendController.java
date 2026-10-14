package com.skytech.controller;

import com.skytech.common.ResponseObject;
import com.skytech.dto.BannerResponse;
import com.skytech.dto.FrontendProductDetailDto;
import com.skytech.dto.FrontendProductDto;
import com.skytech.entity.Category;
import com.skytech.entity.TopMarquee;
import com.skytech.service.FrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("getAllCategory")
    public ResponseEntity<ResponseObject<List<Category>>> getAllCategory() {
        ResponseObject<List<Category>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(frontendService.getAllCategory());
        return ResponseEntity.ok(response);
    }

    @GetMapping("getTop8Products")
    public ResponseEntity<ResponseObject<List<FrontendProductDto>>> getTop8Products() {
        ResponseObject<List<FrontendProductDto>> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(frontendService.getTop8Products());
        return ResponseEntity.ok(response);
    }

    @GetMapping("getProductDetailById/{id}")
    public ResponseEntity<ResponseObject<FrontendProductDetailDto>> getProductDetailById(
            @PathVariable("id") Long id) {
        ResponseObject<FrontendProductDetailDto> response = new ResponseObject<>();
        response.setSuccess(true);
        response.setData(frontendService.getProductDetailById(id));
        return ResponseEntity.ok(response);
    }

}
