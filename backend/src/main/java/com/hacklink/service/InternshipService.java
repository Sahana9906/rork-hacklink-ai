package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.ApplicationStatus;
import com.hacklink.entity.Internship;
import com.hacklink.entity.InternshipApplication;
import com.hacklink.entity.SavedInternship;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.InternshipApplicationRepository;
import com.hacklink.repository.InternshipRepository;
import com.hacklink.repository.SavedInternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InternshipService {
    private final InternshipRepository internshipRepository;
    private final SavedInternshipRepository savedRepository;
    private final InternshipApplicationRepository applicationRepository;
    private final InternshipMatchingService matchingService;
    private final ApiMapper mapper;

    @Transactional(readOnly = true)
    public List<Dtos.InternshipResponse> list(User user) {
        return internshipRepository.findAllByOrderByApplicationDeadlineAsc().stream().map(internship -> mapper.toInternship(internship, user.getId())).toList();
    }

    @Transactional(readOnly = true)
    public Dtos.InternshipResponse get(User user, UUID id) {
        return mapper.toInternship(require(id), user.getId());
    }

    @Transactional(readOnly = true)
    public Dtos.InternshipMatchResponse match(User user, UUID id) {
        return matchingService.match(user, require(id));
    }

    @Transactional
    public Dtos.InternshipResponse save(User user, UUID id) {
        Internship internship = require(id);
        if (savedRepository.findByInternshipIdAndUserId(id, user.getId()).isEmpty()) savedRepository.save(new SavedInternship(internship, user));
        return mapper.toInternship(internship, user.getId());
    }

    @Transactional
    public void unsave(User user, UUID id) {
        savedRepository.findByInternshipIdAndUserId(id, user.getId()).ifPresent(savedRepository::delete);
    }

    @Transactional
    public Dtos.ApplicationResponse apply(User user, UUID id) {
        Internship internship = require(id);
        if (internship.getStatus() != com.hacklink.entity.InternshipStatus.OPEN) throw new ApiException("INTERNSHIP_CLOSED", "This internship is not accepting applications.", HttpStatus.CONFLICT);
        InternshipApplication application = applicationRepository.findByInternshipIdAndUserId(id, user.getId()).orElseGet(() -> new InternshipApplication(internship, user));
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(Instant.now());
        application.setExternalSubmissionConfirmed(false);
        application = applicationRepository.save(application);
        return new Dtos.ApplicationResponse(application.getId(), id, application.getStatus(), application.getAppliedAt(), internship.getApplicationUrl(), false);
    }

    public Internship require(UUID id) {
        return internshipRepository.findById(id).orElseThrow(() -> new ApiException("INTERNSHIP_NOT_FOUND", "Internship was not found.", HttpStatus.NOT_FOUND));
    }
}
