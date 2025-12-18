package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.AdminStatisticResponse;
import com.example.rout24.dto.response.UserStatisticResponse;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.PaymentStatus;
import com.example.rout24.entity.enums.UserRole;
import com.example.rout24.repository.OrderRepository;
import com.example.rout24.repository.RouteRepository;
import com.example.rout24.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final RouteRepository routeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ApiResponse<UserStatisticResponse> getUserStatistics(User user) {
        int routesCount = 0;
        int ordersCount = 0;
        BigDecimal monthlyIncome = null;
        BigDecimal monthlyExpense = null;

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfToday = LocalDateTime.now();

        if (user.getRole() == UserRole.DRIVER) {
            routesCount = routeRepository.countByDriver(user);
            ordersCount = orderRepository.countByRoute_Driver(user);
            monthlyIncome = orderRepository.sumDriverMonthlyIncome(user, PaymentStatus.PAID, startOfMonth, endOfToday);
        } else if (user.getRole() == UserRole.CLIENT) {
            ordersCount = orderRepository.countByClient(user);
            monthlyExpense = orderRepository.sumClientMonthlyExpense(user, PaymentStatus.PAID, startOfMonth, endOfToday);
        }

        UserStatisticResponse response = new UserStatisticResponse(
                routesCount,
                ordersCount,
                monthlyIncome,
                monthlyExpense
        );

        return ApiResponse.success(response, "Foydalanuvchi statistikasi olindi");
    }


    public ApiResponse<AdminStatisticResponse> getAdminStatistics() {
        long usersCount = userRepository.countByRole(UserRole.CLIENT);
        long driversCount = userRepository.countByRole(UserRole.DRIVER);

        BigDecimal moneyCirculation = orderRepository.findAll().stream()
                .map(order -> order.getRoute().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        AdminStatisticResponse response = AdminStatisticResponse.builder()
                .usersCount(usersCount)
                .driversCount(driversCount)
                .moneyCirculation(moneyCirculation)
                .build();

        return ApiResponse.success(response);
    }
}
