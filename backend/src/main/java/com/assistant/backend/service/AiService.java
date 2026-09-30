package com.assistant.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.assistant.backend.dto.ProjectAnalysisResponse;
import com.assistant.backend.dto.ProjectAnalysisRequest;

@Service 
public class AiService {
    private final RestClient restClient;
    
    public AiService() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8000").build();
    }

    public ProjectAnalysisResponse analyzeProject(
        ProjectAnalysisRequest request
    ) {
        return restClient.post()
            .uri("/api/project/analyze")
            .body(request)
            .retrieve()
            .body(ProjectAnalysisResponse.class);
    }
}
