package com.hacklink.repository;

import com.hacklink.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    @Query("select c from Connection c where (c.requester.id = :first and c.receiver.id = :second) or (c.requester.id = :second and c.receiver.id = :first)")
    Optional<Connection> findBetween(@Param("first") UUID first, @Param("second") UUID second);
}
