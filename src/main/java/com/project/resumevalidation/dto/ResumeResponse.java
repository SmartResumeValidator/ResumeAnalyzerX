package com.project.resumevalidation.dto;

import java.util.List;

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
    private List<String> extractedSkills;
    private double matchPercentage;
    private String message;


    public ResumeResponse(String message) {
        this.message = message;
    }

}

