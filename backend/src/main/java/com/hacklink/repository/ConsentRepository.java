package com.hacklink.repository;

import com.hacklink.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsentRepository extends JpaRepository<Consent, UUID> {
}
