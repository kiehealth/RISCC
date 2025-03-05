package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.ResourceFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceFileRepo extends JpaRepository<ResourceFileEntity, Long> {
}
