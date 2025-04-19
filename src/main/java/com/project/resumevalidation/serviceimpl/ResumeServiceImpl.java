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
    
    // Java-specific skills list for training
    private static final List<String> JAVA_SKILLS = Arrays.asList(
        "Java", "Spring", "Spring Boot", "Hibernate", "JPA", "Maven", "Gradle", "JUnit", 
        "Mockito", "TestNG", "Selenium", "REST API", "SOAP", "JSP", "Servlets", "JDBC", 
        "Multithreading", "Concurrency", "Collections", "Streams", "Lambda Expressions", 
        "Generics", "Design Patterns", "Microservices", "Docker", "Kubernetes", "Git", 
        "CI/CD", "Jenkins", "Log4j", "SLF4J", "RabbitMQ", "Kafka", "IBM MQ", "JSON", 
        "XML", "IntelliJ IDEA", "Eclipse", "PostgreSQL", "MySQL", "Oracle SQL", "Redis", 
        "Linux", "Swagger", "OpenAPI", "OAuth2", "JWT", "Apache Tomcat", "JUnit 5", "JAXB", 
        "Jackson", "Quartz Scheduler", "Lombok", "SonarQube", "Flyway", "Liquibase", "Prometheus", 
        "Grafana", "ELK Stack", "JIRA"
    );
 // JavaScript-specific skills list for training
    private static final List<String> JS_SKILLS = Arrays.asList(
        "JavaScript", "TypeScript", "Node.js", "Express", "React", "Redux", "Next.js", 
        "Angular", "Vue.js", "Nuxt.js", "Svelte", "HTML", "CSS", "SCSS", "Tailwind CSS", 
        "Bootstrap", "jQuery", "AJAX", "JSON", "Webpack", "Babel", "ESLint", "Prettier", 
        "NPM", "Yarn", "Jest", "Mocha", "Chai", "Jasmine", "Cypress", "Playwright", 
        "Puppeteer", "GraphQL", "REST API", "WebSockets", "OAuth2", "JWT", "MongoDB", 
        "PostgreSQL", "Firebase", "Supabase", "Git", "Docker", "CI/CD", "Jenkins", 
        "GitLab CI", "VS Code", "Chrome DevTools"
    );
 // Data Engineering-specific skills list
    private static final List<String> DATA_ENGINEERING_SKILLS2 = Arrays.asList(
        "Informatica Cloud (IICS)",
        "SQL",
        "Power BI",
        "Apache Kafka",
        "Kafka Lenses",
        "Autosys",
        "Unix",
        "Python",
        "Salesforce",
        "Azure DevOps (CI/CD Pipeline)",
        "APIM (Azure API Management)",
        "Postman",
        "Apache Spark",
        "Hadoop",
        "Apache Hive",
        "MySQL"
    );

    
    private static final List<String> DATA_ENGINEERING_SKILLS = Arrays.asList(
    	    "Data Engineering", "Apache Hadoop", "Apache Spark", "Apache Kafka", "Apache Flink", "Apache Beam", 
    	    "AWS Redshift", "Google BigQuery", "Snowflake", "Databricks", "Apache Hive", "Presto", "Apache Airflow", 
    	    "Airflow DAGs", "ETL Pipelines", "ELT Pipelines", "SQL", "Python", "Java", "Scala", "SQLAlchemy", "Pandas", 
    	    "Dask", "Jupyter Notebooks", "Data Lakes", "Data Warehouses", "Data Modeling", "Dimensional Modeling", 
    	    "Snowflake Schema", "Star Schema", "Delta Lake", "Apache Parquet", "Apache Avro", "ORC Format", 
    	    "NoSQL Databases", "MongoDB", "Cassandra", "HBase", "Redis", "Elasticsearch", "AWS S3", "Google Cloud Storage", 
    	    "Azure Blob Storage", "AWS Lambda", "Google Cloud Functions", "Kafka Streams", "Apache Pulsar", "Kubernetes", 
    	    "Docker", "CI/CD", "Git", "Terraform", "Jenkins", "GitLab CI", "DevOps", "Hadoop Distributed File System (HDFS)", 
    	    "Apache ZooKeeper", "Apache Oozie", "AWS Glue", "Google Cloud Dataproc", "Azure Data Factory", "AWS Kinesis", 
    	    "Google Cloud Pub/Sub", "Apache Sqoop", "Talend", "Informatica", "Alteryx", "Data Pipeline", "Data Quality", 
    	    "Data Governance", "Data Integration", "Python for Data Engineering", "Spark SQL", "Pandas DataFrames", 
    	    "Dask DataFrames", "PySpark", "Spark Streaming", "Data Transformation", "Data Extraction", "Data Cleansing", 
    	    "Data Enrichment", "Real-Time Data Processing", "Batch Processing", "Data Aggregation", "Cloud Data Engineering", 
    	    "Streaming Data", "Data Orchestration", "Big Data", "Data Pipelines", "Workflow Automation", "Cloud Data Warehousing", 
    	    "Data Shuffling", "SQL Joins", "Data Compression", "Columnar Storage", "Parallel Processing", "Cluster Computing", 
    	    "ETL Tools", "Cloud Databases", "Data Security", "Masking Data", "Encryption", "Anomaly Detection", 
    	    "Real-Time Monitoring", "Data Backup", "Data Recovery", "Data Auditing", "Disaster Recovery", "Query Optimization"
    	);



    @Override
    public ResumeResponse processResume(MultipartFile file, String jobDescription) throws IOException {
        String content = extractTextFromPdf(file);
        
        String email = extractEmail(content);
        String phone = extractPhone(content);
        List<String> skills = extractSkillsWithNER(content); // Using NER-based extraction
        System.out.println("Extracted Skills: " + skills);
        
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
        return matcher.find() ? matcher.group() : "Email Not found";
    }

    private String extractPhone(String text) {
        Matcher matcher = Pattern.compile("\\b\\d{10}\\b").matcher(text);
        return matcher.find() ? matcher.group() : "Phone Number Not found";
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
                String skill = skillBuilder.toString().trim();
                System.out.println("skill " +skill);
////                if (JAVA_SKILLS.contains(skill)) {
////                    foundSkills.add(skill);
////                }
//                 if (JS_SKILLS.contains(skill)) {
//                    foundSkills.add(skill);
//                }
////                else if (DATA_ENGINEERING_SKILLS.contains(skill)) {
////                    foundSkills.add(skill);
////                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundSkills.isEmpty() ? extractSkillsFromKeywords(text) : foundSkills;
    }

    // If NER doesn't find any skills, fall back to keyword-based extraction
  
        private List<String> extractSkillsFromKeywords(String text) {
            String lowerText = text.toLowerCase();
            List<String> foundSkills = new ArrayList<>();
            
            // Search Java, JS, and Data Engineering skills
            foundSkills.addAll(JAVA_SKILLS.stream()
                    .filter(skill -> lowerText.contains(skill.toLowerCase()))
                    .collect(Collectors.toList()));
            foundSkills.addAll(JS_SKILLS.stream()
                    .filter(skill -> lowerText.contains(skill.toLowerCase()))
                    .collect(Collectors.toList()));

            foundSkills.addAll(DATA_ENGINEERING_SKILLS.stream()
                    .filter(skill -> lowerText.contains(skill.toLowerCase()))
                    .collect(Collectors.toList()));
            
            return foundSkills;
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

    private List<String> extractSkillsFromText(String text) {
        String lowerText = text.toLowerCase();
        List<String> foundSkills = new ArrayList<>();
        
        // Add Java skills
        foundSkills.addAll(JAVA_SKILLS.stream()
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .collect(Collectors.toList()));
        
        // Add Java skills
        foundSkills.addAll(JS_SKILLS.stream()
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .collect(Collectors.toList()));
        
        
        // Add Data Engineering skills
        foundSkills.addAll(DATA_ENGINEERING_SKILLS.stream()
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .collect(Collectors.toList()));
        
        return foundSkills;
    }

}
