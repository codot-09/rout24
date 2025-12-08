package com.example.rout24.repository;

import com.example.rout24.entity.Order;
import com.example.rout24.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    boolean existsByRoute(Route route);
    List<Order> findAllByRoute(Route route);

}
