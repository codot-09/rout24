package org.example.rout24.component;

import lombok.RequiredArgsConstructor;
import org.example.rout24.entity.enums.Role;
import org.example.rout24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create")){
            User admin = User.builder()
                    .phone("998900000000")
                    .password(encoder.encode("admin"))
                    .role(Role.ADMIN)
                    .authType(AuthType.PASSWORD)
                    .firstName("Admin")
                    .lastName("Admin")
                    .build();

            userRepository.save(admin);
        }
    }
}
