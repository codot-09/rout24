package com.example.rout24.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.BannerDTO;
import com.example.rout24.dto.response.BannerPreviewResponse;
import com.example.rout24.service.BannerService;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
@Tag(name = "Bannerlar")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBanner(@RequestBody BannerDTO bannerDTO){
        return ResponseEntity.ok(bannerService.createBanner(bannerDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BannerPreviewResponse>>> getBanners(){
        return ResponseEntity.ok(bannerService.getBanners());
    }

    @GetMapping("/{bannerId}")
    public ResponseEntity<ApiResponse<BannerDTO>> getById(@PathVariable UUID bannerId){
        return ResponseEntity.ok(bannerService.getById(bannerId));
    }

}
