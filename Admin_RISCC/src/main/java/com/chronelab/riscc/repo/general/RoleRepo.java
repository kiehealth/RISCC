package com.chronelab.riscc.repo.general;

import com.chronelab.riscc.entity.general.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByTitle(String title);
}
