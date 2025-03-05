package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepo extends JpaRepository<SettingEntity, Long> {
}
