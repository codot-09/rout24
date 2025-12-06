package com.example.rout24.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private String title;
    private String message;
    private String type;
    private LocalDateTime createdAt;

}
