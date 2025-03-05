package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.QuestionnaireEntity;
import com.chronelab.riscc.repo.projection.QuestionnaireProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionnaireRepo extends JpaRepository<QuestionnaireEntity, Long> {
    QuestionnaireEntity findByTitle(String title);

    Page<QuestionnaireProjection> findAllByCreatedDateNotNull(Pageable pageable);

    List<QuestionnaireProjection> findAllByCreatedDateNotNull(Sort sort);
}
