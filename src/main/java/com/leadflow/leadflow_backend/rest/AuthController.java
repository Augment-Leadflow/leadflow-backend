package com.leadflow.leadflow_backend.rest;

import com.leadflow.leadflow_backend.dto.LoginRequest;
import com.leadflow.leadflow_backend.dto.LoginResponse;
import com.leadflow.leadflow_backend.dto.RegisterRequest;
import com.leadflow.leadflow_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        log.info("Received login request");

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody
            RegisterRequest request
    ) {

        log.info(
                "Register request received for email: {}",
                request.getEmail()
        );

        return ResponseEntity
                .status(201)
                .body(authService.register(request));
    }

}