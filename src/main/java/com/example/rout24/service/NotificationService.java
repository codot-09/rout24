package com.example.rout24.service;

import java.util.Comparator;
import java.util.List;
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

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendNotification(User user, String title, String message,NotificationType type) {
        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .user(user)
                .type(type)
                .build();

        notificationRepository.save(notification);
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
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Notification> notifications = notificationRepository.findByUserChatId(user.getChatId());

            if (notifications.size() > 20) {
                List<Notification> toDelete = notifications.stream()
                        .sorted(Comparator.comparing(Notification::getCreatedAt))
                        .limit(notifications.size() - 20)
                        .toList();

                notificationRepository.deleteAll(toDelete);
            }
        }
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
