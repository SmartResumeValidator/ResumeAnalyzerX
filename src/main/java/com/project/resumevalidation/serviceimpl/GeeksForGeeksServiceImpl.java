package com.project.resumevalidation.serviceimpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import com.project.resumevalidation.service.GeeksForGeeksService;

@Service
public class GeeksForGeeksServiceImpl implements GeeksForGeeksService {

    @Override
    public int getProblemsSolved(String username) {
        try {
            String url = "https://www.geeksforgeeks.org/user/" + username + "/";

            // Enhanced headers to bypass restrictions
            Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .referrer("https://www.google.com/")
                .timeout(15000)
                .get();

            // Exact selector based on the HTML you provided
            Element countElement = document.selectFirst("div.circularProgressBar_head_mid_streakCnt__MFOF1");
            
            if (countElement != null) {
                // Get the direct text (184) before the span
                String countText = countElement.ownText().trim();
                return Integer.parseInt(countText);
            } else {
                System.err.println("Element not found - GfG may have updated their structure");
                return -1;
            }

        } catch (Exception e) {
            System.err.println("Error scraping GeeksforGeeks: " + e.getMessage());
            return -1;
        }
    }
}