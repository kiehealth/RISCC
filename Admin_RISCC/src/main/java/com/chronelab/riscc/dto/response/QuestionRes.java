package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionRes {
    private Long id;
    private String title;
    private String body;
    private String researchId;
    private String questionTypeResearchId;

    private QuestionTypeRes questionType;
    private List<QuestionnaireRes> questionnaires;

    private List<QuestionOptionRes> questionOptions;
    private List<AnswerRes> answers;

    private List<QuestionQuestionnaireRes> questionQuestionnaires;

    public QuestionRes setId(Long id) {
        this.id = id;
        return this;
    }

    public QuestionRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionRes setBody(String body) {
        this.body = body;
        return this;
    }

    public QuestionRes setResearchId(String researchId) {
        this.researchId = researchId;
        return this;
    }

    public QuestionRes setQuestionTypeResearchId(String questionTypeResearchId) {
        this.questionTypeResearchId = questionTypeResearchId;
        return this;
    }

    public QuestionRes setQuestionType(QuestionTypeRes questionType) {
        this.questionType = questionType;
        return this;
    }

    public QuestionRes setQuestionnaires(List<QuestionnaireRes> questionnaires) {
        this.questionnaires = questionnaires;
        return this;
    }

    public QuestionRes setQuestionOptions(List<QuestionOptionRes> questionOptions) {
        this.questionOptions = questionOptions;
        return this;
    }

    public QuestionRes setAnswers(List<AnswerRes> answers) {
        this.answers = answers;
        return this;
    }

    public QuestionRes setQuestionQuestionnaires(List<QuestionQuestionnaireRes> questionQuestionnaires) {
        this.questionQuestionnaires = questionQuestionnaires;
        return this;
    }
}
