package com.example.rout24.repository;

import com.example.rout24.entity.DriverInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<DriverInfo, UUID> {
    Optional<DriverInfo> findByDriverChatId(String driverId);
    
    @Query("""
    SELECT d FROM DriverInfo d
    WHERE d.status = com.example.rout24.entity.enums.RequestStatus.PENDING
    AND (:from IS NULL OR d.driver.registeredAt >= :from)
    AND (:to IS NULL OR d.driver.registeredAt <= :to)
    AND (:userId IS NULL OR d.driver.chatId = :userId)
    """)
    Page<DriverInfo> findUnverifiedDriverInfos(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("userId") String userId,
            Pageable pageable
    );

}
