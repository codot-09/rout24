package com.example.rout24.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";

    @Value("${cloud.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String uploadImage(MultipartFile file) {
        validateImageFile(file);
        
        log.debug("Starting image upload for file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", apiKey);
            body.add("image", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(UPLOAD_URL, requestEntity, String.class);

            JsonNode urlNode = objectMapper.readTree(response).path("data").path("url");
            if (urlNode.isMissingNode() || urlNode.asText().isBlank()) {
                log.error("Failed to get image URL from ImgBB response");
                throw new RuntimeException("Rasm yuklanmadi: URL topilmadi");
            }
            
            String imageUrl = urlNode.asText();
            log.debug("Image uploaded successfully: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Error reading image file: {}", e.getMessage());
            throw new RuntimeException("Rasmni o'qishda xatolik: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error uploading image to ImgBB: {}", e.getMessage());
            throw new RuntimeException("ImgBB server bilan aloqa xatosi: " + e.getMessage(), e);
        }
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Empty or null file upload attempt");
            throw new IllegalArgumentException("Fayl bo'sh yoki yuklanmagan");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            log.warn("File size exceeds maximum: {} bytes (max: {} bytes)", file.getSize(), MAX_FILE_SIZE);
            throw new IllegalArgumentException("Fayl hajmi 5MB dan oshmasligi kerak");
        }
        String type = file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            log.warn("Invalid file type: {}", type);
            throw new IllegalArgumentException("Faqat rasm fayllari qabul qilinadi");
        }
    }
}
