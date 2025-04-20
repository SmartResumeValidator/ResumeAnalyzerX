package com.project.resumevalidation.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.resumevalidation.service.LeetCodeService;

@Service
public class LeetcodeServiceImpl implements LeetCodeService {
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public LeetcodeServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("https://leetcode.com").build();
        this.objectMapper = objectMapper;
    }

    @Override
    public int getProblemsSolved(String username) {
        try {
            String graphqlQuery = String.format("{\"query\":\"query getUserProfile($username: String!) { matchedUser(username: $username) { submitStats: submitStatsGlobal { acSubmissionNum { difficulty count } } } }\",\"variables\":{\"username\":\"%s\"}}", username);
            
            String response = webClient.post()
                .uri("/graphql")
                .header("Content-Type", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .bodyValue(graphqlQuery)
                .retrieve()
                .bodyToMono(String.class)
                .block();
                
            JsonNode root = objectMapper.readTree(response);
            JsonNode acSubmissionNum = root.path("data")
                .path("matchedUser")
                .path("submitStats")
                .path("acSubmissionNum");
                
            int totalSolved = 0;
            for (JsonNode node : acSubmissionNum) {
                if ("All".equals(node.path("difficulty").asText())) {
                    totalSolved = node.path("count").asInt();
                    break;
                }
            }
            return totalSolved;
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}