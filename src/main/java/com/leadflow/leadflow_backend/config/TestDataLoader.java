package com.leadflow.leadflow_backend.config;

import com.leadflow.leadflow_backend.domain.Role;
import com.leadflow.leadflow_backend.model.User;
import com.leadflow.leadflow_backend.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        if(userRepository.findByEmail(
                "admin@example.com"
        ).isEmpty()) {

            BCryptPasswordEncoder encoder =
                    new BCryptPasswordEncoder();

            User user = User.builder()
                    .email("admin@example.com")
                    .password(
                            encoder.encode("password123")
                    )
                    .role(Role.ADMIN)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            System.out.println(
                    "Admin user inserted successfully."
            );
        }
    }
}