package com.example.rout24.entity;

import com.example.rout24.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cars", indexes = {
    @Index(name = "idx_driver_chat_id_vehicle", columnList = "driver_chat_id"),
    @Index(name = "idx_plate_number", columnList = "plateNumber")
})
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_chat_id", nullable = false, unique = true)
    private User driver;

    private String plateNumber;

    private String name;

    private VehicleType type;

    @ElementCollection
    @CollectionTable(name = "vehicle_images", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "image_url")
    private List<String> images;
}
