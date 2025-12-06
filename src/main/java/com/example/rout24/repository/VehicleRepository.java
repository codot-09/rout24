package com.example.rout24.repository;

import com.example.rout24.entity.User;
import com.example.rout24.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByDriver(User driver);
    boolean existsByDriver(User driver);
    boolean existsByPlateNumber(String plateNumber);
}
