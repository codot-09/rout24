package com.example.rout24.repository;

import com.example.rout24.entity.Order;
import com.example.rout24.entity.Route;
import com.example.rout24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    boolean existsByRoute(Route route);
    List<Order> findAllByRoute(Route route);
    Optional<Order> findByBillingNumber(Integer billingNumber);

    int countByClient(User client);

    int countByRoute_User(User driver);

    @Query("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.client = :client AND o.paymentStatus = 'PAID' AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumClientMonthlyExpense(@Param("client") User client, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.route.user = :driver AND o.paymentStatus = 'PAID' AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumDriverMonthlyIncome(@Param("driver") User driver, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
