package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppAnalyticsKeyRepo extends JpaRepository<AppAnalyticsKeyEntity, Long>, JpaSpecificationExecutor<AppAnalyticsKeyEntity> {
}
