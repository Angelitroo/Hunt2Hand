package com.hunt2hand.repository;

import com.hunt2hand.model.ContrasenaReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContrasenaResetRepository extends JpaRepository<ContrasenaReset, Long> {
    Optional<ContrasenaReset> findByToken(String token);
}