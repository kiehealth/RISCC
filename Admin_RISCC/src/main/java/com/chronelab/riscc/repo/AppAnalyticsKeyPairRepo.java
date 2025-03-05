package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyPairEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppAnalyticsKeyPairRepo extends JpaRepository<AppAnalyticsKeyPairEntity, Long> {
    Optional<AppAnalyticsKeyPairEntity> findByKeyOneOrKeyTwo(AppAnalyticsKeyEntity typeOne, AppAnalyticsKeyEntity typeTwo);

    List<AppAnalyticsKeyPairEntity> findAllByKeyTwoNotNull(Sort sort);

    void deleteAllByKeyOneOrKeyTwo(AppAnalyticsKeyEntity appAnalyticsKeyEntity1, AppAnalyticsKeyEntity appAnalyticsKeyEntity2);
}
