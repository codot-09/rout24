package com.example.rout24.repository;

import com.example.rout24.entity.Order;
import com.example.rout24.entity.Route;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.PaymentStatus;
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
    Optional<Order> findByQrCode(String qrCode);

    int countByClient(User client);

    int countByRoute_Driver(User driver);

    @Query("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.client = :client AND o.paymentStatus = :status AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumClientMonthlyExpense(@Param("client") User client,
                                       @Param("status") PaymentStatus status,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(o.price), 0) FROM Order o WHERE o.route.driver = :driver AND o.paymentStatus = :status AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumDriverMonthlyIncome(@Param("driver") User driver,
                                      @Param("status") PaymentStatus status,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}
