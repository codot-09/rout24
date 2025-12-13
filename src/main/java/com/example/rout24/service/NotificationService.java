package com.example.rout24.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.example.rout24.bot.BotService;
import com.example.rout24.entity.Order;
import com.example.rout24.entity.enums.OrderStatus;
import com.example.rout24.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.NotificationResponse;
import com.example.rout24.entity.Notification;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.NotificationType;
import com.example.rout24.repository.NotificationRepository;
import com.example.rout24.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final BotService botService;
    private final OrderRepository orderRepository;

    @Transactional
    public void sendNotification(User user, String title, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .user(user)
                .type(type)
                .build();

        notificationRepository.save(notification);
        log.debug("Notification sent to user {}: {}", user.getChatId(), title);
    }

    public void sendDriverNotification(String chatId,String message){
        botService.sendMessage(Long.valueOf(chatId),message);
    }

    @Scheduled(fixedRate = 60000)
    public void sendNotification() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.WAITING);
        LocalDateTime now = LocalDateTime.now();

        for (Order order : orders) {
            if (order.getRoute().getDepartureDate() != null && order.getClient() != null) {
                Duration duration = Duration.between(now, order.getRoute().getDepartureDate());
                long minutesLeft = duration.toMinutes();

                if (minutesLeft > 0 && minutesLeft <= 30) {
                    String message = "✈️ Sizning reysingiz: " + order.getRoute().getFromRegion() + " -> " + order.getRoute().getToRegion() +
                            "\n🕒 Jo'nash vaqti: " + order.getRoute().getDepartureDate() +
                            "\n⏳ Faqat 30 daqiqa qoldi!";
                    botService.sendMessage(Long.valueOf(order.getClient().getChatId()), message);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<NotificationResponse>> getNotifications(User user) {
        List<Notification> notifications = notificationRepository.findByUserChatId(user.getChatId());

        List<NotificationResponse> responses = notifications.stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.success(responses);
    }

    @Transactional(readOnly = true)
    public ApiResponse<Integer> getNotificationsCount(User user) {
        return ApiResponse.success(notificationRepository.getCountByUserChatId(user.getChatId()));
    }

    @Transactional
    public ApiResponse<String> clearNotifications(User user) {
        notificationRepository.deleteAllByUserChatId(user.getChatId());
        return ApiResponse.success("Bildirishnomalar tozalandi");
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoClearNotifications() {
        log.info("Starting automatic notification cleanup");
        List<User> users = userRepository.findAll();
        int totalDeleted = 0;

        for (User user : users) {
            List<Notification> notifications = notificationRepository.findByUserChatId(user.getChatId());
            
            if (notifications.size() > 20) {
                int deleteCount = notifications.size() - 20;
                List<Notification> toDelete = notifications.stream()
                        .sorted(Comparator.comparing(Notification::getCreatedAt))
                        .limit(deleteCount)
                        .toList();

                notificationRepository.deleteAll(toDelete);
                totalDeleted += deleteCount;
                log.debug("Deleted {} notifications for user {}", deleteCount, user.getChatId());
            }
        }
        
        log.info("Automatic notification cleanup completed. Total deleted: {}", totalDeleted);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType().name());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
