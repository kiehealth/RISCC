package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_question")
@DynamicInsert
@DynamicUpdate
public class QuestionEntity extends CommonEntity {

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "question_type_id", nullable = false)
    private QuestionTypeEntity questionType;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<QuestionOptionEntity> questionOptions;

    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private List<AnswerEntity> answers;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<QuestionQuestionnaireEntity> questionQuestionnaires;

    public QuestionEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionEntity setBody(String body) {
        this.body = body;
        return this;
    }

    public QuestionEntity setQuestionType(QuestionTypeEntity questionType) {
        this.questionType = questionType;
        return this;
    }

    public QuestionEntity setQuestionOptions(List<QuestionOptionEntity> questionOptions) {
        this.questionOptions = questionOptions;
        return this;
    }

    public QuestionEntity setAnswers(List<AnswerEntity> answers) {
        this.answers = answers;
        return this;
    }

    public QuestionEntity setQuestionQuestionnaires(List<QuestionQuestionnaireEntity> questionQuestionnaires) {
        this.questionQuestionnaires = questionQuestionnaires;
        return this;
    }
}
