package com.example.stackly_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceController {

    @RequestMapping("/")
    public String Space() {
        return "Hello World";
    }
}
