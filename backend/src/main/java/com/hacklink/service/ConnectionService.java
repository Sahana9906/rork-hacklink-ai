package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Connection;
import com.hacklink.entity.ConnectionStatus;
import com.hacklink.entity.NotificationType;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.ConnectionRepository;
import com.hacklink.repository.ProfileRepository;
import com.hacklink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final NotificationService notificationService;
    private final ApiMapper mapper;

    @Transactional
    public Dtos.ConnectionResponse request(User requester, UUID receiverId) {
        if (requester.getId().equals(receiverId)) throw new ApiException("SELF_CONNECTION", "You cannot connect with yourself.", HttpStatus.BAD_REQUEST);
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new ApiException("USER_NOT_FOUND", "User was not found.", HttpStatus.NOT_FOUND));
        if (profileRepository.findByUserId(receiverId).map(profile -> !profile.isDiscoverable()).orElse(true)) throw new ApiException("USER_NOT_DISCOVERABLE", "This user is not accepting discovery requests.", HttpStatus.FORBIDDEN);
        if (connectionRepository.findBetween(requester.getId(), receiverId).isPresent()) throw new ApiException("DUPLICATE_CONNECTION", "A connection already exists between these users.", HttpStatus.CONFLICT);
        Connection connection = connectionRepository.save(new Connection(requester, receiver));
        notificationService.create(receiver, NotificationType.CONNECTION_REQUEST, "New connection request", requester.getEmail() + " wants to connect with you.", connection.getId());
        return mapper.toConnection(connection);
    }

    @Transactional
    public Dtos.ConnectionResponse accept(User receiver, UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(() -> new ApiException("CONNECTION_NOT_FOUND", "Connection was not found.", HttpStatus.NOT_FOUND));
        if (!connection.getReceiver().getId().equals(receiver.getId())) throw new ApiException("FORBIDDEN", "You cannot accept this request.", HttpStatus.FORBIDDEN);
        if (connection.getStatus() != ConnectionStatus.PENDING) throw new ApiException("CONNECTION_NOT_PENDING", "This connection is no longer pending.", HttpStatus.CONFLICT);
        connection.setStatus(ConnectionStatus.ACCEPTED);
        connection.setUpdatedAt(Instant.now());
        return mapper.toConnection(connectionRepository.save(connection));
    }

    @Transactional
    public Dtos.ConnectionResponse reject(User receiver, UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(() -> new ApiException("CONNECTION_NOT_FOUND", "Connection was not found.", HttpStatus.NOT_FOUND));
        if (!connection.getReceiver().getId().equals(receiver.getId())) throw new ApiException("FORBIDDEN", "You cannot reject this request.", HttpStatus.FORBIDDEN);
        if (connection.getStatus() != ConnectionStatus.PENDING) throw new ApiException("CONNECTION_NOT_PENDING", "This connection is no longer pending.", HttpStatus.CONFLICT);
        connection.setStatus(ConnectionStatus.REJECTED);
        connection.setUpdatedAt(Instant.now());
        return mapper.toConnection(connectionRepository.save(connection));
    }

    @Transactional
    public void delete(User user, UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(() -> new ApiException("CONNECTION_NOT_FOUND", "Connection was not found.", HttpStatus.NOT_FOUND));
        if (!connection.getRequester().getId().equals(user.getId()) && !connection.getReceiver().getId().equals(user.getId())) throw new ApiException("FORBIDDEN", "You cannot delete this connection.", HttpStatus.FORBIDDEN);
        connectionRepository.delete(connection);
    }
}
