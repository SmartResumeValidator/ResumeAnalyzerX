package com.project.resumevalidation.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.project.resumevalidation.dto.ResumeResponse;

public interface ResumeService {
    ResumeResponse processResume(MultipartFile file, String jobDescription) throws IOException;
}
