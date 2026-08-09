package com.hacklink.integration;

import com.hacklink.entity.Profile;

import java.util.List;

public interface AIProfileAnalyzer {
    String explain(Profile profile, List<String> matchedSkills, List<String> missingSkills);
}
