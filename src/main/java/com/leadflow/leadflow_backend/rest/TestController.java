package com.leadflow.leadflow_backend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secure")
    public ResponseEntity<String> secureApi() {

        return ResponseEntity.ok(
                "Protected API accessed successfully"
        );
    }
}