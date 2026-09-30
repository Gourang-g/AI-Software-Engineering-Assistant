package com.assistant.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping ("/api/test")
    public String test() {
        return "AI Software Engineering Assistant Backend is running!";
    }
}
