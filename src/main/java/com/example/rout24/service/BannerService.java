package com.example.rout24.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.BannerDTO;
import com.example.rout24.dto.response.BannerPreviewResponse;
import com.example.rout24.entity.Banner;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.repository.BannerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    public ApiResponse<String> createBanner(BannerDTO request){
        Banner banner = Banner.builder()
            .coverImage(request.getCoverImage())
            .title(request.getTitle())
            .description(request.getDescription())
            .externalLink(request.getExternalLink() != null ? request.getExternalLink() : null)
            .build();

        bannerRepository.save(banner);

        return ApiResponse.success(null,"Banner yaratildi");
    }

    public ApiResponse<List<BannerPreviewResponse>> getBanners(){
        List<Banner> banners = bannerRepository.findAll();

        List<BannerPreviewResponse> response = banners.stream()
            .map(this::mapToPreview)
            .toList();

        return ApiResponse.success(response);
    }

    public ApiResponse<BannerDTO> getById(UUID id){
        Banner banner = bannerRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Banner topilmadi"));

        return ApiResponse.success(mapToResponse(banner));
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoClearBanners() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<Banner> oldBanners = bannerRepository.findByCreatedAtBefore(oneMonthAgo);
        bannerRepository.deleteAll(oldBanners);
    }

    private BannerDTO mapToResponse(Banner banner){
        BannerDTO dto = new BannerDTO();
        dto.setId(banner.getId());
        dto.setCoverImage(banner.getCoverImage());
        dto.setTitle(banner.getTitle());
        dto.setDescription(banner.getDescription());
        dto.setExternalLink(banner.getExternalLink());
        dto.setCreatedAt(banner.getCreatedAt());

        return dto;
    }

    private BannerPreviewResponse mapToPreview(Banner banner){
        BannerPreviewResponse response = new BannerPreviewResponse();
        response.setId(banner.getId());
        response.setCoverImage(banner.getCoverImage());

        return response;
    }

}
