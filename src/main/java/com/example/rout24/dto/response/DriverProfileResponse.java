package com.example.rout24.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverProfileResponse {

    private String fullName;
    private String imageUrl;
    private Boolean status;
    private Boolean premiumUser;
    private String plateNumber;
    private String coverImage;
}
