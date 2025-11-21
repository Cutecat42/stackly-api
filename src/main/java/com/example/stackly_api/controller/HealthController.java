package com.example.stackly_api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    @CrossOrigin(origins = "http://localhost:3000")
    public String healthCheck() {
        return "OK";
    }
}

