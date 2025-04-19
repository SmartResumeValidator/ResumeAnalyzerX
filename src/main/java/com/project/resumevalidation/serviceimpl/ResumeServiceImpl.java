package com.project.resumevalidation.serviceimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.resumevalidation.dto.ResumeResponse;
import com.project.resumevalidation.service.ResumeService;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

@Service
public class ResumeServiceImpl implements ResumeService {

	private static final String NER_MODEL_PATH = "models/en-ner-technology.bin";

    @Override
    public ResumeResponse processResume(MultipartFile file, String jobDescription) throws IOException {
        String content = extractTextFromPdf(file);
        //System.out.println(content);

        String email = extractEmail(content);
        String phone = extractPhone(content);
        List<String> skills = extractSkillsWithNER(content); // Replace the method call to use NER-based extraction
        System.out.println(skills);
        double matchPercentage = calculateMatchPercentage(jobDescription, skills);

        ResumeResponse response = new ResumeResponse();
        response.setEmail(email);
        response.setPhone(phone);
        response.setExtractedSkills(skills);
        response.setMatchPercentage(matchPercentage);
        response.setMessage("Resume processed successfully");

        return response;
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String extractEmail(String text) {
        Matcher matcher = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
        return matcher.find() ? matcher.group() : "Not found";
    }

    private String extractPhone(String text) {
        Matcher matcher = Pattern.compile("\\b\\d{10}\\b").matcher(text);
        return matcher.find() ? matcher.group() : "Not found";
    }
    private List<String> extractSkillsWithNER(String text) throws IOException {
        List<String> foundSkills = new ArrayList<>();

        // Tokenize the resume text
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        try (InputStream modelIn = getClass().getClassLoader().getResourceAsStream(NER_MODEL_PATH)) {
            if (modelIn == null) {
                throw new IOException("Model file not found!");
            }

            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);

            Span[] spans = nameFinder.find(tokens);

            for (Span span : spans) {
                StringBuilder skillBuilder = new StringBuilder();
                for (int i = span.getStart(); i < span.getEnd(); i++) {
                    skillBuilder.append(tokens[i]).append(" ");
                }
                foundSkills.add(skillBuilder.toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundSkills.isEmpty() ? extractSkillsFromKeywords(text) : filterSkills(foundSkills);
    }


    // If NER doesn't find any skills, fall back to basic keyword extraction (optional)
    private List<String> extractSkillsFromKeywords(String text) {
        List<String> skillsList = Arrays.asList("Java", "Spring Boot", "MySQL", "AWS", "Hibernate", "Kafka", "MongoDB");
        String lowerText = text.toLowerCase();

        return skillsList.stream()
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .collect(Collectors.toList());
    }
    private List<String> filterSkills(List<String> skills) {
        return skills.stream()
            .map(String::trim)
            .filter(skill -> skill.length() > 1)
            .filter(skill -> VALID_SKILLS.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(skill)
                    || valid.toLowerCase().contains(skill.toLowerCase())
                    || skill.toLowerCase().contains(valid.toLowerCase())))
            .distinct()
            .collect(Collectors.toList());
    }

    private List<String> extractSkillsFromText(String text) {
        return VALID_SKILLS.stream()
            .filter(skill -> text.toLowerCase().contains(skill.toLowerCase()))
            .collect(Collectors.toList());
    }


    private double calculateMatchPercentage(String jobDescription, List<String> resumeSkills) {
        List<String> jdSkills = extractSkillsFromText(jobDescription);

        long matched = resumeSkills.stream()
            .map(String::toLowerCase)
            .filter(skill -> jdSkills.stream()
                .map(String::toLowerCase)
                .anyMatch(jdSkill -> jdSkill.equals(skill) 
                    || jdSkill.contains(skill) 
                    || skill.contains(jdSkill)))
            .count();

        return jdSkills.isEmpty() ? 0.0 : ((double) matched / jdSkills.size()) * 100;
    }

    
    private static final List<String> VALID_SKILLS = Arrays.asList(
    	    // === Programming Languages ===
    	    "Java", "Python", "JavaScript", "TypeScript", "C++", "C#", "Go", "Ruby", "PHP", "Scala", "Kotlin", "Swift",

    	    // === Backend Frameworks ===
    	    "Spring Boot", "Spring MVC", "Hibernate", "Node.js", "Express", "Django", "Flask", "ASP.NET", "Micronaut", "Quarkus",

    	    // === Frontend Frameworks ===
    	    "React", "Angular", "Vue.js", "Svelte", "Next.js", "jQuery", "Bootstrap", "Tailwind CSS",

    	    // === Databases ===
    	    "MySQL", "PostgreSQL", "MongoDB", "Oracle", "Redis", "SQLite", "Cassandra", "DynamoDB", "Elasticsearch",

    	    // === DevOps / Tools ===
    	    "Git", "GitHub", "GitLab", "Bitbucket", "Jenkins", "Docker", "Kubernetes", "Terraform", "Ansible", "CI/CD", "Nginx",

    	    // === Cloud Platforms ===
    	    "AWS", "Azure", "GCP", "Firebase", "Heroku", "Cloudflare",

    	    // === Testing / QA ===
    	    "JUnit", "Mockito", "Selenium", "Postman", "Cypress", "TestNG", "JMeter",

    	    // === APIs and Integration ===
    	    "REST API", "GraphQL", "WebSocket", "SOAP", "OAuth", "gRPC", "RabbitMQ", "Kafka", "ActiveMQ",

    	    // === Other / Miscellaneous ===
    	    "Linux", "Shell Scripting", "Splunk", "Prometheus", "Grafana", "Logstash", "Kibana", "New Relic", "DataDog",

    	    // === Architecture / Patterns ===
    	    "Microservices", "Monolith", "Event-driven", "Serverless", "MVC", "DDD", "TDD", "Agile", "Scrum", "Kanban"
    	);

}
