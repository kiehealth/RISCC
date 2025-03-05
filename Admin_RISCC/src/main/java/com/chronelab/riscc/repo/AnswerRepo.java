package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.AnswerEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepo extends JpaRepository<AnswerEntity, Long>, JpaSpecificationExecutor<AnswerEntity> {
    Page<AnswerEntity> findAllByUserAndGroupQuestionnaire(UserEntity userEntity, GroupQuestionnaireEntity groupQuestionnaireEntity, Pageable pageable);

    List<AnswerEntity> findAllByUserAndGroupQuestionnaire(UserEntity userEntity, GroupQuestionnaireEntity groupQuestionnaireEntity, Sort sort);

    boolean existsByUserAndQuestionAndGroupQuestionnaire(UserEntity userEntity, QuestionEntity questionEntity, GroupQuestionnaireEntity groupQuestionnaireEntity);

    boolean existsByUserAndQuestionAndGroupQuestionnaireAndCreatedDateBetween(UserEntity userEntity, QuestionEntity questionEntity, GroupQuestionnaireEntity groupQuestionnaireEntity,
                                                                              LocalDateTime groupQuestionnaireStartDate,
                                                                              LocalDateTime groupQuestionnaireEndDate);

    void deleteAllByUser(UserEntity userEntity);

    List<AnswerEntity> findAllByCreatedDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2, Sort sort);

    int countAllByUserAndGroupQuestionnaire(UserEntity userEntity, GroupQuestionnaireEntity groupQuestionnaireEntity);
}
