package com.project.resumevalidation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodingProfileExtractor {

    public static String extractLeetCodeUsername(String resumeText) {
        return extractUsername(resumeText, "LeetCode", "Leetcode");
    }

    public static String extractGeeksforGeeksUsername(String resumeText) {
        return extractUsername(resumeText, "GeeksforGeeks", "GeeksForGeeks", "GFG");
    }

    private static String extractUsername(String resumeText, String... patterns) {
        if (resumeText == null || resumeText.isEmpty()) {
            return null;
        }

        // Try each possible pattern variation
        for (String pattern : patterns) {
            // Case 1: "LeetCode: username"
            String regex1 = "(?i)" + pattern + "[:\\s]+([\\w-]+)";
            // Case 2: "LeetCode - username"
            String regex2 = "(?i)" + pattern + "[\\s-]+([\\w-]+)";
            
            Matcher matcher1 = Pattern.compile(regex1).matcher(resumeText);
            Matcher matcher2 = Pattern.compile(regex2).matcher(resumeText);
            
            if (matcher1.find()) {
                return matcher1.group(1).trim();
            }
            if (matcher2.find()) {
                return matcher2.group(1).trim();
            }
        }
        
        return null;
    }
}
