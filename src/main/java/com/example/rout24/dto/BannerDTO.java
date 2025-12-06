package com.example.rout24.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String coverImage;
    private String title;
    private String description;
    private String externalLink;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate createdAt;

}
