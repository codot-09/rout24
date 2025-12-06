package com.example.rout24.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.rout24.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner,UUID>{
    List<Banner> findByCreatedAtBefore(LocalDate date);

}
