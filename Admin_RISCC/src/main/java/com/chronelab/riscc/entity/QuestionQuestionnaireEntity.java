package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_question_questionnaire")
@DynamicInsert
@DynamicUpdate
public class QuestionQuestionnaireEntity extends CommonEntity {

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private QuestionnaireEntity questionnaire;

    public QuestionQuestionnaireEntity setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public QuestionQuestionnaireEntity setQuestion(QuestionEntity question) {
        this.question = question;
        return this;
    }

    public QuestionQuestionnaireEntity setQuestionnaire(QuestionnaireEntity questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }
}
