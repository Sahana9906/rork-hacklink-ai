package com.hacklink.service;

import com.hacklink.dto.Dtos;
import com.hacklink.entity.Notification;
import com.hacklink.entity.NotificationType;
import com.hacklink.entity.User;
import com.hacklink.exception.ApiException;
import com.hacklink.mapper.ApiMapper;
import com.hacklink.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ApiMapper mapper;

    @Transactional
    public void create(User user, NotificationType type, String title, String body, UUID referenceId) {
        notificationRepository.save(new Notification(user, type, title, body, referenceId));
    }

    @Transactional(readOnly = true)
    public List<Dtos.NotificationResponse> list(User user) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream().map(mapper::toNotification).toList();
    }

    @Transactional
    public void markRead(User user, UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ApiException("NOTIFICATION_NOT_FOUND", "Notification was not found.", HttpStatus.NOT_FOUND));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new ApiException("FORBIDDEN", "You cannot modify this notification.", HttpStatus.FORBIDDEN);
        }
        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllRead(User user) {
        notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream().filter(notification -> notification.getReadAt() == null).forEach(notification -> notification.setReadAt(Instant.now()));
    }
}
