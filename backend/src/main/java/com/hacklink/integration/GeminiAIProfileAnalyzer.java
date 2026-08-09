package com.hacklink.integration;

import com.hacklink.entity.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiAIProfileAnalyzer implements AIProfileAnalyzer {
    private final String apiKey;

    public GeminiAIProfileAnalyzer(@Value("${hacklink.gemini.api-key:}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String explain(Profile profile, List<String> matchedSkills, List<String> missingSkills) {
        // The deterministic score and evidence are always computed by domain services. Gemini is only an optional explainer.
        if (apiKey == null || apiKey.isBlank()) {
            return "Your profile matches " + String.join(", ", matchedSkills) + "; strengthen " + String.join(", ", missingSkills) + ".";
        }
        return "Your evidence-backed profile aligns with " + String.join(", ", matchedSkills) + ". Consider building evidence for " + String.join(", ", missingSkills) + ".";
    }
}
