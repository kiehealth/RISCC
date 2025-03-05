package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.GroupQuestionnaireAnswerFinishedEntity;
import com.chronelab.riscc.entity.GroupQuestionnaireEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupQuestionnaireAnswerFinishedRepo extends JpaRepository<GroupQuestionnaireAnswerFinishedEntity, Long> {
    List<GroupQuestionnaireAnswerFinishedEntity> findAllByUserAndGroupQuestionnaireAndFinishedDateTimeBetween(
            UserEntity userEntity, GroupQuestionnaireEntity groupQuestionnaireEntity, LocalDateTime start, LocalDateTime end
    );

    List<GroupQuestionnaireAnswerFinishedEntity> findAllByUser_IdAndGroupQuestionnaire_IdAndFinishedDateTimeBetween(
            Long userId, Long groupQuestionnaireId, LocalDateTime start, LocalDateTime end
    );

    void deleteAllByUser(UserEntity userEntity);
}
