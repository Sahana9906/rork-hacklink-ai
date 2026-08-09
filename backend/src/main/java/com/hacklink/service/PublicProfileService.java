package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicProfileService {
    private final ProfileRepository profileRepository;
    private final ApiMapper mapper;

    @Transactional(readOnly = true)
    public Dtos.PublicProfileResponse get(UUID publicProfileId) {
        return profileRepository.findByPublicProfileId(publicProfileId)
                .filter(profile -> profile.isDiscoverable())
                .map(mapper::toPublicProfile)
                .orElseThrow(() -> new ApiException("PUBLIC_PROFILE_NOT_FOUND", "This public profile is not available.", HttpStatus.NOT_FOUND));
    }
}
