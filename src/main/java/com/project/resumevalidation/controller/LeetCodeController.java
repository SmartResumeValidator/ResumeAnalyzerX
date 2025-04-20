package com.project.resumevalidation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.resumevalidation.service.GeeksForGeeksService;
import com.project.resumevalidation.service.LeetCodeService;


@RestController
@RequestMapping("/api/codingProfiles")
public class LeetCodeController {

    private final LeetCodeService leetCodeService;
    private final GeeksForGeeksService forGeeksService;

    @Autowired
    public LeetCodeController(LeetCodeService leetCodeService, GeeksForGeeksService forGeeksService) {
    	this.forGeeksService= forGeeksService;
        this.leetCodeService = leetCodeService;
    }

    @GetMapping("/leetcode/user/{username}/solved")
    public ResponseEntity<String> getSolvedProblems(@PathVariable String username) {
        try {
            int solvedProblems = leetCodeService.getProblemsSolved(username);
            return ResponseEntity.ok("Problems solved by " + username + ": " + solvedProblems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching solved problems for " + username + ": " + e.getMessage());
        }
    }
    @GetMapping("/gfg/user/{username}/solved")
    public ResponseEntity<String> getSolvedProblemsGfg(@PathVariable String username) {
        try {
            int solvedProblems = forGeeksService.getProblemsSolved(username);
            return ResponseEntity.ok("Problems solved by " + username + ": " + solvedProblems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching solved problems for " + username + ": " + e.getMessage());
        }
    }
}

