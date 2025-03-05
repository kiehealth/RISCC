package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.QuestionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionTypeRepo extends JpaRepository<QuestionTypeEntity, Long> {
    Optional<QuestionTypeEntity> findByTitle(String title);
}
