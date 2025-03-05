package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_answer")
@DynamicInsert
@DynamicUpdate
public class AnswerEntity extends CommonEntity {

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "group_questionnaire_id", nullable = false)
    private GroupQuestionnaireEntity groupQuestionnaire;

    @OneToMany(mappedBy = "answer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AnswerOptionEntity> answerOptions;

    public AnswerEntity setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public AnswerEntity setQuestion(QuestionEntity question) {
        this.question = question;
        return this;
    }

    public AnswerEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public AnswerEntity setGroupQuestionnaire(GroupQuestionnaireEntity groupQuestionnaire) {
        this.groupQuestionnaire = groupQuestionnaire;
        return this;
    }

    public AnswerEntity setAnswerOptions(List<AnswerOptionEntity> answerOptions) {
        this.answerOptions = answerOptions;
        return this;
    }
}
