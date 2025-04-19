package com.project.resumevalidation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.resumevalidation.dto.ResumeResponse;
import com.project.resumevalidation.service.ResumeService;

@RestController
@RequestMapping("/api/resume")
public class PdfController {

    private final ResumeService resumeService;

    @Autowired
    public PdfController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping(value ="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponse> uploadResume(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("jobDescription") String jobDescription) {
        try {
            ResumeResponse response = resumeService.processResume(file, jobDescription);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResumeResponse("Error processing resume: " + e.getMessage()));
        }
    }
}

