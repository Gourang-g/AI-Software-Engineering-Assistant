package com.assistant.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assistant.backend.dto.ProjectAnalysisRequest;
import com.assistant.backend.dto.ProjectAnalysisResponse;
import com.assistant.backend.service.AiService;

@RestController 
@RequestMapping ("/api/project")
public class AIController {

    private final AiService aiService;

    public AIController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping ("/analyze")
    public ProjectAnalysisResponse analyzeProject(@RequestBody ProjectAnalysisRequest request) {
        return aiService.analyzeProject(request);
    }
}
    