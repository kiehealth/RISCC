package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkRepo extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByTitle(String title);
}
