package com.hacklink.integration;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.User;

public interface GithubService {
    Dtos.GithubResponse connectUser(User user, Dtos.GithubConnectRequest request);
    Dtos.GithubResponse getUserProfile(User user);
    Dtos.GithubResponse getRepositories(User user);
    Dtos.GithubResponse syncRepositories(User user);
    void disconnectUser(User user);
}
