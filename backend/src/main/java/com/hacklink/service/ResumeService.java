package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Resume;
import com.hacklink.entity.ResumeStatus;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private final ResumeRepository resumeRepository;
    private final ResumeParserService parserService;
    private final SkillExtractionService skillExtractionService;
    private final ProfileStrengthService profileStrengthService;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.ResumeResponse upload(User user, MultipartFile file) {
        validate(file);
        Resume resume = resumeRepository.save(new Resume(user, file.getOriginalFilename() == null ? "resume.pdf" : file.getOriginalFilename(), file.getContentType(), file.getSize()));
        try {
            ResumeParserService.ParsedResume parsed = parserService.parse(file.getBytes());
            List<Dtos.SkillResponse> skills = skillExtractionService.extractFromResume(user, resume, parsed);
            resume.setStatus(ResumeStatus.PARSED);
            resume.setParsedAt(Instant.now());
            resumeRepository.save(resume);
            profileStrengthService.recalculate(user.getId());
            return new Dtos.ResumeResponse(resume.getId(), resume.getOriginalFileName(), resume.getFileSize(), resume.getStatus(), resume.getUploadedAt(), resume.getParsedAt(), skills);
        } catch (IOException | RuntimeException exception) {
            resume.setStatus(ResumeStatus.FAILED);
            resumeRepository.save(resume);
            if (exception instanceof ApiException apiException) throw apiException;
            throw new ApiException("RESUME_PARSE_FAILED", "The resume was uploaded but could not be parsed.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional(readOnly = true)
    public Dtos.ResumeResponse latest(User user) {
        Resume resume = resumeRepository.findTopByUserIdOrderByUploadedAtDesc(user.getId())
                .orElseThrow(() -> new ApiException("RESUME_NOT_FOUND", "No resume has been uploaded.", HttpStatus.NOT_FOUND));
        return new Dtos.ResumeResponse(resume.getId(), resume.getOriginalFileName(), resume.getFileSize(), resume.getStatus(), resume.getUploadedAt(), resume.getParsedAt(), List.of());
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("RESUME_REQUIRED", "A PDF resume is required.", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException("FILE_TOO_LARGE", "Resume files must be 5 MB or smaller.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase();
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!"application/pdf".equals(contentType) && !name.endsWith(".pdf")) {
            throw new ApiException("PDF_REQUIRED", "Only PDF resumes are supported.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
