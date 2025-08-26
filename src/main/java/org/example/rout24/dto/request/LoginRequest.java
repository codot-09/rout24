package org.example.rout24.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String chatId;
    private String fullName;
    private String region;
    private String city;
    private String phone;
    private String imageUrl;
}
