package com.hacklink.integration;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.User;

public interface LinkedInService {
    void connect(User user, Dtos.LinkedInConnectRequest request);
    void disconnect(User user);
}
