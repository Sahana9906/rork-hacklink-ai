package com.hacklink.service;

import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ConnectionRepository;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {
    @Mock ConnectionRepository connectionRepository;
    @Mock UserRepository userRepository;
    @Mock ProfileRepository profileRepository;
    @Mock NotificationService notificationService;
    @Mock ApiMapper mapper;

    @Test
    void rejectsSelfConnectionBeforeRepositoryMutation() {
        UUID userId = UUID.randomUUID();
        com.hacklink.entity.User user = new com.hacklink.entity.User();
        user.setId(userId);
        ConnectionService service = new ConnectionService(connectionRepository, userRepository, profileRepository, notificationService, mapper);

        assertThatThrownBy(() -> service.request(user, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("You cannot connect with yourself.");
    }
}
