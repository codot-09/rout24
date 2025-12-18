package com.example.rout24.entity;

import com.example.rout24.entity.enums.Gender;
import com.example.rout24.entity.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "driver_infos")
public class DriverInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_chat_id", nullable = false, unique = true)
    private User driver;

    private String driverLicense;

    private String passportId;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(unique = true)
    private String phoneNumber;
}
