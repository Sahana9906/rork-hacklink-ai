package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.HackathonSkill;
import com.hacklink.entity.Skill;
import com.hacklink.entity.User;
import com.hacklink.entity.UserSkill;
import com.hacklink.repository.HackathonSkillRepository;
import com.hacklink.repository.UserSkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HackathonMatchingServiceTest {
    @Mock
    private HackathonSkillRepository hackathonSkillRepository;
    @Mock
    private UserSkillRepository userSkillRepository;

    @Test
    void calculatesDeterministicWeightedScoreAndGaps() {
        UUID userId = UUID.randomUUID();
        UUID hackathonId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Hackathon hackathon = new Hackathon();
        hackathon.setId(hackathonId);
        Skill java = new Skill("Java");
        Skill cloud = new Skill("Cloud");
        HackathonSkill javaRequirement = new HackathonSkill(hackathon, java, 3, true);
        HackathonSkill cloudRequirement = new HackathonSkill(hackathon, cloud, 1, true);
        when(userSkillRepository.findAllByUserId(userId)).thenReturn(List.of(new UserSkill(user, java, 90)));
        when(hackathonSkillRepository.findAllByHackathonId(hackathonId)).thenReturn(List.of(javaRequirement, cloudRequirement));

        Dtos.HackathonMatchResponse result = new HackathonMatchingService(hackathonSkillRepository, userSkillRepository).match(user, hackathon);

        assertThat(result.matchScore()).isEqualTo(75);
        assertThat(result.matchedSkills()).containsExactly("Java");
        assertThat(result.missingSkills()).containsExactly("Cloud");
    }
}
