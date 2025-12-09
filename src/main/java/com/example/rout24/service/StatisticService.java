package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.UserStatisticResponse;
import com.example.rout24.entity.Order;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.PaymentStatus;
import com.example.rout24.entity.enums.UserRole;
import com.example.rout24.repository.OrderRepository;
import com.example.rout24.repository.RouteRepository;
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

    public ApiResponse<UserStatisticResponse> getUserStatistics(User user) {
        int routesCount = routeRepository.countByUser(user);
        int ordersCount;
        BigDecimal monthlyIncome = null;
        BigDecimal monthlyExpense = null;
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfToday = LocalDateTime.now();

        if (user.getRole() == UserRole.DRIVER) {
            ordersCount = orderRepository.countByRoute_User(user);
            monthlyIncome = orderRepository.sumDriverMonthlyIncome(user, startOfMonth, endOfToday);
        } else {
            ordersCount = orderRepository.countByClient(user);
            monthlyExpense = orderRepository.sumClientMonthlyExpense(user, startOfMonth, endOfToday);
        }

        UserStatisticResponse response = new UserStatisticResponse(
                routesCount,
                ordersCount,
                monthlyIncome,
                monthlyExpense
        );

        return ApiResponse.success(response);
    }
}
