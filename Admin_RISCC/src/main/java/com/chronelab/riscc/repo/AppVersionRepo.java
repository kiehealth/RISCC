package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AppVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepo extends JpaRepository<AppVersionEntity, Long> {
}
