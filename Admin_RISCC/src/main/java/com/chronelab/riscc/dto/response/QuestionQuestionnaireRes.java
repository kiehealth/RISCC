package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionQuestionnaireRes {
    private Long id;
    private Integer displayOrder;
    private QuestionnaireRes questionnaire;
    private QuestionRes question;

    public QuestionQuestionnaireRes setId(Long id) {
        this.id = id;
        return this;
    }

    public QuestionQuestionnaireRes setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public QuestionQuestionnaireRes setQuestionnaire(QuestionnaireRes questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }

    public QuestionQuestionnaireRes setQuestion(QuestionRes question) {
        this.question = question;
        return this;
    }
}
