package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AppAnalyticsEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AppAnalyticsRepo extends JpaRepository<AppAnalyticsEntity, Long>, JpaSpecificationExecutor<AppAnalyticsEntity> {

    Page<AppAnalyticsEntity> findAllByUser_Id(Long userId, Pageable pageable);

    List<AppAnalyticsEntity> findAllByUser_Id(Long userId, Sort sort);

    Page<AppAnalyticsEntity> findAllByAppAnalyticsKey_Id(Long appAnalyticsKeyId, Pageable pageable);

    List<AppAnalyticsEntity> findAllByAppAnalyticsKey_Id(Long appAnalyticsKeyId, Sort sort);

    Page<AppAnalyticsEntity> findAllByAppAnalyticsKey_IdAndUser_Id(Long appAnalyticsKeyId, Long userId, Pageable pageable);

    List<AppAnalyticsEntity> findAllByAppAnalyticsKey_IdAndUser_Id(Long appAnalyticsKeyId, Long userId, Sort sort);

    void deleteAllByUser(UserEntity userEntity);

    void deleteAllByAppAnalyticsKey(AppAnalyticsKeyEntity appAnalyticsKeyEntity);
}
