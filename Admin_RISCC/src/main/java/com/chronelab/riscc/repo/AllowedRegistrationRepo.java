package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AllowedRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllowedRegistrationRepo extends JpaRepository<AllowedRegistrationEntity, Long> {
    Optional<AllowedRegistrationEntity> findByEmail(String email);
}
