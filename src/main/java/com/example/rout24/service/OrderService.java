package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.OrderCreateRequest;
import com.example.rout24.dto.response.OrderResponse;
import com.example.rout24.dto.response.PagedResponse;
import com.example.rout24.entity.BillingNumberSeq;
import com.example.rout24.entity.Order;
import com.example.rout24.entity.Route;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.OrderStatus;
import com.example.rout24.entity.enums.Regions;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.exception.InvalidRequestException;
import com.example.rout24.repository.BillingNumberSeqRepository;
import com.example.rout24.repository.OrderRepository;
import com.example.rout24.repository.RouteRepository;
import com.example.rout24.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RouteRepository routeRepository;
    private final BillingNumberSeqRepository billingNumberSeqRepository;
    private final QRCodeService qrCodeService;

    public ApiResponse<String> createOrder(User client, OrderCreateRequest request){
        Route route = routeRepository.findById(request.getRoutId())
                .orElseThrow(() -> new DataNotFoundException("Reys topilmadi"));

        if (route.getSeatsCount() == 0){
            throw new InvalidRequestException("Reys to'lgan");
        }

        if (orderRepository.existsByRoute(route)){
            throw new InvalidRequestException("Oldin zakaz qilingan");
        }

        BigDecimal price = route.getPrice()
                .multiply(BigDecimal.valueOf(request.getSeatsCount()));

        Integer billingNumber = generateBillingNumber();

        Order order = Order.builder()
                .client(client)
                .route(route)
                .paymentType(request.getPaymentType())
                .seatsCount(request.getSeatsCount())
                .price(price)
                .qrCode(qrCodeService.generateQRCodeUrl(billingNumber))
                .billingNumber(billingNumber)
                .status(OrderStatus.WAITING)
                .build();

        orderRepository.save(order);

        return ApiResponse.success(null,"Buyurtma qabul qilindi");
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<OrderResponse>> getOrdersByRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new DataNotFoundException("Reys topilmadi"));

        List<Order> orders = orderRepository.findAllByRoute(route);

        List<OrderResponse> content = orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setSeatsCount(order.getSeatsCount());
            response.setClientName(order.getClient() != null ? order.getClient().getFullName() : null);
            response.setPrice(order.getPrice());
            response.setOrderDate(order.getOrderDate());
            response.setPaid(order.isPaid());
            response.setBillingNumber(order.getBillingNumber());
            response.setPaymentType(order.getPaymentType() != null ? order.getPaymentType() : null);
            response.setQrCode(order.getQrCode());
            response.setRouteId(order.getRoute().getId());
            response.setStatus(order.getStatus());
            return response;
        }).toList();

        return ApiResponse.success(content, "Buyurtmalar topildi");
    }

    @Transactional(readOnly = true)
    public ApiResponse<PagedResponse<OrderResponse>> getOwnOrders(
            User client,
            Regions from,
            Regions to,
            OrderStatus status,
            Pageable pageable
    ) {
        var spec = OrderSpecification.filter(client.getChatId(), from, to, status);
        var ordersPage = orderRepository.findAll(spec, pageable);

        List<OrderResponse> content = ordersPage.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(ordersPage.getNumber());
        pagedResponse.setPageSize(ordersPage.getSize());
        pagedResponse.setTotalElements(ordersPage.getTotalElements());
        pagedResponse.setTotalPages(ordersPage.getTotalPages());
        pagedResponse.setLast(ordersPage.isLast());

        return ApiResponse.success(pagedResponse, "Buyurtmalar topildi");
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setSeatsCount(order.getSeatsCount());
        response.setClientName(order.getClient() != null ? order.getClient().getFullName() : null);
        response.setPrice(order.getPrice());
        response.setOrderDate(order.getOrderDate());
        response.setPaid(order.isPaid());
        response.setBillingNumber(order.getBillingNumber());
        response.setPaymentType(order.getPaymentType() != null ? order.getPaymentType() : null);
        response.setQrCode(order.getQrCode());
        response.setStatus(order.getStatus());
        return response;
    }

    @Transactional
    private Integer generateBillingNumber() {
        BillingNumberSeq seq = billingNumberSeqRepository.findById(1)
                .orElseGet(() -> {
                    BillingNumberSeq s = new BillingNumberSeq();
                    s.setValue(100000L);
                    return billingNumberSeqRepository.save(s);
                });

        long nextValue = seq.getValue() + 1;
        seq.setValue(nextValue);

        return (int) nextValue;
    }
}
