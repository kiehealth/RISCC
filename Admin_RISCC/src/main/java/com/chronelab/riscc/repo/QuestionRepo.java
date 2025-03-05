package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {

    Optional<QuestionEntity> findByTitleAndBody(String title, String body);

    QuestionEntity findByTitle(String title);

    Page<QuestionEntity> findAllByQuestionQuestionnaires_QuestionnaireIn(List<QuestionnaireEntity> questionnaireEntities, Pageable pageable);

    List<QuestionEntity> findAllByQuestionQuestionnaires_QuestionnaireIn(List<QuestionnaireEntity> questionnaireEntities, Sort sort);

    @Query(value = "SELECT * FROM tbl_question q,  tbl_question_questionnaire qq " +
            "WHERE q.id = qq.question_id AND" +
            " qq.questionnaire_id = :questionnaireId AND q.id NOT IN " +
            "(SELECT question_id from tbl_answer WHERE user_id = :userId AND group_questionnaire_id = :groupQuestionnaireId " +
            "AND tbl_answer.created_date > :groupQuestionnaireStartDate AND tbl_answer.created_date < :groupQuestionnaireEndDate)",
            countQuery = "SELECT COUNT(q.id) FROM tbl_question AS q,  tbl_question_questionnaire qq " +
                    " WHERE q.id = qq.question_id AND " +
                    " qq.questionnaire_id = :questionnaireId AND q.id NOT IN " +
                    "(SELECT question_id from tbl_answer WHERE user_id = :userId AND group_questionnaire_id = :groupQuestionnaireId " +
                    "AND tbl_answer.created_date > :groupQuestionnaireStartDate AND tbl_answer.created_date < :groupQuestionnaireEndDate)",
            nativeQuery = true)
    Page<QuestionEntity> unansweredQuestions(
            @Param("questionnaireId") Long questionnaireId,
            @Param("userId") Long userId,
            @Param("groupQuestionnaireId") Long groupQuestionnaireId,
            @Param("groupQuestionnaireStartDate") LocalDateTime groupQuestionnaireStartDate,
            @Param("groupQuestionnaireEndDate") LocalDateTime groupQuestionnaireEndDate,
            Pageable pageable);

    @Query(value = "SELECT * FROM tbl_question q,  tbl_question_questionnaire qq " +
            "WHERE q.id = qq.question_id AND" +
            " qq.questionnaire_id = :questionnaireId AND q.id NOT IN " +
            "(SELECT question_id from tbl_answer WHERE user_id = :userId AND group_questionnaire_id = :groupQuestionnaireId " +
            " AND tbl_answer.created_date > :groupQuestionnaireStartDate AND tbl_answer.created_date < :groupQuestionnaireEndDate)",
            nativeQuery = true)
    List<QuestionEntity> unansweredQuestions(
            @Param("questionnaireId") Long questionnaireId,
            @Param("userId") Long userId,
            @Param("groupQuestionnaireId") Long groupQuestionnaireId,
            @Param("groupQuestionnaireStartDate") LocalDateTime groupQuestionnaireStartDate,
            @Param("groupQuestionnaireEndDate") LocalDateTime groupQuestionnaireEndDate
    );

    Page<QuestionEntity> findDistinctByTitleNotNull(Pageable pageable);

    List<QuestionEntity> findDistinctByTitleNotNull(Sort sort);

}
