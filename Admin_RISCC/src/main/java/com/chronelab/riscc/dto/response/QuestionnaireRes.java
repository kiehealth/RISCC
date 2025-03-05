package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionnaireRes {
    private Long id;
    private String title;

    private List<QuestionQuestionnaireRes> questionQuestionnaires;

    private List<QuestionRes> questions;

    public QuestionnaireRes setId(Long id) {
        this.id = id;
        return this;
    }

    public QuestionnaireRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionnaireRes setQuestionQuestionnaires(List<QuestionQuestionnaireRes> questionQuestionnaires) {
        this.questionQuestionnaires = questionQuestionnaires;
        return this;
    }

    public QuestionnaireRes setQuestions(List<QuestionRes> questions) {
        this.questions = questions;
        return this;
    }
}
