package com.hacklink.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ResumeParserService {
    private static final List<String> KNOWN_SKILLS = List.of(
            "Java", "Spring Boot", "Spring", "Kotlin", "Android", "Python", "JavaScript", "TypeScript", "React",
            "PostgreSQL", "SQL", "REST APIs", "Git", "Docker", "Kubernetes", "AWS", "Azure", "GCP", "Cloud",
            "AI/ML", "Machine Learning", "Gemini", "TensorFlow", "Figma", "UI/UX", "Node.js", "C++"
    );

    public ParsedResume parse(byte[] content) {
        try (PDDocument document = Loader.loadPDF(content)) {
            String text = new PDFTextStripper().getText(document);
            Set<String> skills = new LinkedHashSet<>();
            String lowerText = text.toLowerCase(Locale.ROOT);
            for (String skill : KNOWN_SKILLS) {
                if (lowerText.contains(skill.toLowerCase(Locale.ROOT))) {
                    skills.add(skill);
                }
            }
            List<String> projects = text.lines()
                    .map(String::trim)
                    .filter(line -> line.length() >= 4 && line.length() <= 180)
                    .filter(line -> line.toLowerCase(Locale.ROOT).contains("project") || line.toLowerCase(Locale.ROOT).contains("built "))
                    .limit(10)
                    .toList();
            return new ParsedResume(text, new ArrayList<>(skills), projects);
        } catch (IOException exception) {
            throw new IllegalArgumentException("The PDF could not be parsed", exception);
        }
    }

    public record ParsedResume(String text, List<String> skills, List<String> projectLines) {
    }
}
