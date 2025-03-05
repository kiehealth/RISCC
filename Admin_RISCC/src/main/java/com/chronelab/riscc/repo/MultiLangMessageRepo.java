package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.MultiLangMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MultiLangMessageRepo extends JpaRepository<MultiLangMessageEntity, Long> {
    Optional<MultiLangMessageEntity> findByCode(String code);
}
