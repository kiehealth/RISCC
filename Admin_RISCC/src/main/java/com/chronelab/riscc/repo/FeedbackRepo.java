package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.FeedbackEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<FeedbackEntity, Long>, JpaSpecificationExecutor<FeedbackEntity> {
    Page<FeedbackEntity> findAllByUser(UserEntity userEntity, Pageable pageable);

    List<FeedbackEntity> findAllByUser(UserEntity userEntity, Sort sort);
}
