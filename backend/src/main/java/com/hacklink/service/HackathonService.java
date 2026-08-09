package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Hackathon;
import com.hacklink.entity.HackathonRegistration;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.HackathonRegistrationRepository;
import com.hacklink.repository.HackathonRepository;
import com.hacklink.repository.HackathonSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final HackathonSkillRepository hackathonSkillRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final HackathonMatchingService matchingService;
    private final ApiMapper mapper;

    @Transactional(readOnly = true)
    public List<Dtos.HackathonResponse> list(User user) {
        return hackathonRepository.findAllByOrderByRegistrationDeadlineAsc().stream().map(hackathon -> mapper.toHackathon(hackathon, user.getId(), registrationRepository.findByHackathonIdAndUserId(hackathon.getId(), user.getId()).isPresent())).toList();
    }

    @Transactional(readOnly = true)
    public Dtos.HackathonResponse get(User user, UUID id) {
        Hackathon hackathon = require(id);
        return mapper.toHackathon(hackathon, user.getId(), registrationRepository.findByHackathonIdAndUserId(id, user.getId()).isPresent());
    }

    @Transactional(readOnly = true)
    public List<Dtos.HackathonSkillResponse> skills(UUID id) {
        require(id);
        return hackathonSkillRepository.findAllByHackathonId(id).stream().map(mapper::toHackathonSkill).toList();
    }

    @Transactional
    public Dtos.HackathonResponse register(User user, UUID id) {
        Hackathon hackathon = require(id);
        if (hackathon.getRegistrationDeadline().isBefore(Instant.now())) {
            throw new ApiException("REGISTRATION_CLOSED", "Hackathon registration has closed.", HttpStatus.CONFLICT);
        }
        if (registrationRepository.findByHackathonIdAndUserId(id, user.getId()).isEmpty()) {
            registrationRepository.save(new HackathonRegistration(hackathon, user));
        }
        return mapper.toHackathon(hackathon, user.getId(), true);
    }

    @Transactional(readOnly = true)
    public Dtos.HackathonMatchResponse match(User user, UUID id) {
        return matchingService.match(user, require(id));
    }

    public Hackathon require(UUID id) {
        return hackathonRepository.findById(id).orElseThrow(() -> new ApiException("HACKATHON_NOT_FOUND", "Hackathon was not found.", HttpStatus.NOT_FOUND));
    }
}
