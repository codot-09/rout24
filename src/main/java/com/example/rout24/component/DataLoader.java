package com.example.rout24.component;

import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.UserRole;
import com.example.rout24.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        String adminChatId = "7193645528";
        boolean exists = userRepository.existsById(adminChatId);

        if (!exists) {
            User admin = User.builder()
                    .chatId(adminChatId)
                    .fullName("Admin User")
                    .tgUsername("admin")
                    .role(UserRole.ADMIN)
                    .blocked(false)
                    .premiumUser(true)
                    .build();

            userRepository.save(admin);
        }
    }
}
