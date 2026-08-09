package com.hacklink.integration;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Consent;
import com.hacklink.entity.LinkedInAccount;
import com.hacklink.entity.User;
import com.hacklink.repository.ConsentRepository;
import com.hacklink.repository.LinkedInAccountRepository;
import com.hacklink.security.TokenCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkedInServiceImpl implements LinkedInService {
    private final LinkedInAccountRepository accountRepository;
    private final ConsentRepository consentRepository;
    private final TokenCipher tokenCipher;

    @Override
    @Transactional
    public void connect(User user, Dtos.LinkedInConnectRequest request) {
        LinkedInAccount account = accountRepository.findByUserId(user.getId()).orElseGet(() -> new LinkedInAccount(user, request.subjectId(), request.displayName(), tokenCipher.encrypt(request.accessToken())));
        account.setSubjectId(request.subjectId());
        account.setDisplayName(request.displayName());
        account.setAccessTokenEncrypted(tokenCipher.encrypt(request.accessToken()));
        accountRepository.save(account);
        consentRepository.save(new Consent(user, "LINKEDIN", request.scope()));
    }

    @Override
    @Transactional
    public void disconnect(User user) {
        accountRepository.findByUserId(user.getId()).ifPresent(account -> {
            accountRepository.delete(account);
            consentRepository.findAll().stream().filter(consent -> consent.getUser().getId().equals(user.getId()) && "LINKEDIN".equals(consent.getProvider()) && consent.getRevokedAt() == null).forEach(consent -> {
                consent.setRevokedAt(java.time.Instant.now());
                consentRepository.save(consent);
            });
        });
    }
}
