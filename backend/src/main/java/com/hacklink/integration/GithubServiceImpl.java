package com.hacklink.integration;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.GithubAccount;
import com.hacklink.entity.GithubRepository;
import com.hacklink.entity.Project;
import com.hacklink.entity.SkillSource;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.GithubAccountRepository;
import com.hacklink.repository.GithubRepositoryRepository;
import com.hacklink.repository.ProjectRepository;
import com.hacklink.security.TokenCipher;
import com.hacklink.service.ProfileStrengthService;
import com.hacklink.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubServiceImpl implements GithubService {
    private final GithubAccountRepository accountRepository;
    private final GithubRepositoryRepository repositoryRepository;
    private final ProjectRepository projectRepository;
    private final TokenCipher tokenCipher;
    private final SkillService skillService;
    private final ProfileStrengthService profileStrengthService;
    private final ApiMapper mapper;
    private final WebClient.Builder webClientBuilder;
    @Value("${hacklink.github.api-base-url:https://api.github.com}")
    private String apiBaseUrl;

    @Override
    @Transactional
    public Dtos.GithubResponse connectUser(User user, Dtos.GithubConnectRequest request) {
        GithubAccount account = accountRepository.findByUserId(user.getId()).orElseGet(() -> new GithubAccount(user, request.githubUserId(), request.username(), tokenCipher.encrypt(request.accessToken())));
        account.setGithubUserId(request.githubUserId());
        account.setUsername(request.username());
        account.setAccessTokenEncrypted(tokenCipher.encrypt(request.accessToken()));
        account = accountRepository.save(account);
        profileStrengthService.recalculate(user.getId());
        return toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Dtos.GithubResponse getUserProfile(User user) {
        return toResponse(requireAccount(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Dtos.GithubResponse getRepositories(User user) {
        return toResponse(requireAccount(user));
    }

    @Override
    @Transactional
    public Dtos.GithubResponse syncRepositories(User user) {
        GithubAccount account = requireAccount(user);
        List<GithubRepositoryPayload> repositories;
        try {
            repositories = webClientBuilder.baseUrl(apiBaseUrl).build().get().uri("/user/repos?per_page=100&sort=updated")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenCipher.decrypt(account.getAccessTokenEncrypted()))
                    .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<GithubRepositoryPayload>>() {})
                    .block();
        } catch (RuntimeException exception) {
            throw new ApiException("GITHUB_SYNC_FAILED", "GitHub repositories could not be synchronized.", HttpStatus.BAD_GATEWAY);
        }
        if (repositories != null) {
            for (GithubRepositoryPayload payload : repositories) {
                GithubRepository repository = repositoryRepository.findByGithubAccountIdAndExternalId(account.getId(), String.valueOf(payload.id()))
                        .orElseGet(() -> new GithubRepository(account, String.valueOf(payload.id()), payload.name()));
                repository.setName(payload.name());
                repository.setDescription(payload.description());
                repository.setUrl(payload.html_url());
                repository.setPrimaryLanguage(payload.language());
                repository.setStars(payload.stargazers_count());
                repository.setFork(payload.fork());
                repository.setUpdatedAt(payload.updated_at());
                repositoryRepository.save(repository);
                projectRepository.save(new Project(user, payload.name(), payload.description(), payload.html_url(), SkillSource.GITHUB));
                if (payload.language() != null && !payload.language().isBlank()) {
                    skillService.addSkill(user, payload.language(), 65, SkillSource.GITHUB, payload.name() + " repository", "Primary language detected from an authorized GitHub repository.", payload.html_url());
                }
            }
        }
        account.setLastSyncedAt(Instant.now());
        accountRepository.save(account);
        profileStrengthService.recalculate(user.getId());
        return toResponse(account);
    }

    @Override
    @Transactional
    public void disconnectUser(User user) {
        accountRepository.findByUserId(user.getId()).ifPresent(accountRepository::delete);
    }

    private GithubAccount requireAccount(User user) {
        return accountRepository.findByUserId(user.getId()).orElseThrow(() -> new ApiException("GITHUB_NOT_CONNECTED", "GitHub is not connected.", HttpStatus.NOT_FOUND));
    }

    private Dtos.GithubResponse toResponse(GithubAccount account) {
        List<Dtos.ProjectResponse> repositories = repositoryRepository.findAllByGithubAccountId(account.getId()).stream()
                .map(repository -> new Project(repository.getGithubAccount().getUser(), repository.getName(), repository.getDescription(), repository.getUrl(), SkillSource.GITHUB))
                .map(mapper::toProject).toList();
        return new Dtos.GithubResponse(account.getId(), account.getGithubUserId(), account.getUsername(), account.getConnectedAt(), account.getLastSyncedAt(), repositories);
    }

    private record GithubRepositoryPayload(long id, String name, String description, String html_url, String language, int stargazers_count, boolean fork, Instant updated_at) {
    }
}
