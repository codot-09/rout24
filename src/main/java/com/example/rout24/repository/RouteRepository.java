package com.example.rout24.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.rout24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.example.rout24.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route,UUID>, JpaSpecificationExecutor<Route> {

    List<Route> findAllByFinishedFalseAndDepartureDateBefore(LocalDate date);
    int countByDriver(User driver);

}
