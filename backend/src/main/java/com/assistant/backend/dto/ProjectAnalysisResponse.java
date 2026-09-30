package com.assistant.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class ProjectAnalysisResponse {

    private String project;
    private String status;
    private String message;
    
}
