package com.assistant.backend.controller;


import com.assistant.backend.service.ProjectUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectUploadService uploadService;

    public ProjectController(ProjectUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
        @RequestParam("file") MultipartFile file) {
            try {
                return ResponseEntity.ok(uploadService.uploadAndScan(file));
            } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Could not process ZIP file"));
        }
    }
}