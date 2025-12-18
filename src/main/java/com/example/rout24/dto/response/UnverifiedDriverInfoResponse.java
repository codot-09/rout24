package com.example.rout24.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedDriverInfoResponse {

    private UUID id;
    private String driverName;
    private String username;
    private String driverLicense;
    private String passportId;
    private LocalDate birthDate;
    private String gender;
    private String phone;

}
