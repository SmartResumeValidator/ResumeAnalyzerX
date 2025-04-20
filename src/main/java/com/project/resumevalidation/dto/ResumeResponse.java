package com.project.resumevalidation.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {
    private String name;
    private String email;
    private String phone;
    private Set<String> extractedSkills;
    private double matchPercentage;
    private String leetcodeUsername;  // New field
    private String geeksforgeeksUsername;  // New field
    private String message;


    public ResumeResponse(String message) {
        this.message = message;
    }

}

