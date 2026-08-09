package com.hacklink.repository;

import com.hacklink.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
