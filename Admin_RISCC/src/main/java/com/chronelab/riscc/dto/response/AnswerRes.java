package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerRes {
    private Long id;
    private String answer;
    private String researchId;
    private List<AnswerOptionRes> answerOptions;
    private QuestionRes question;
    private UserResDto user;
    private QuestionnaireRes questionnaire;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    public AnswerRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AnswerRes setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public AnswerRes setResearchId(String researchId) {
        this.researchId = researchId;
        return this;
    }

    public AnswerRes setAnswerOptions(List<AnswerOptionRes> answerOptions) {
        this.answerOptions = answerOptions;
        return this;
    }

    public AnswerRes setQuestion(QuestionRes question) {
        this.question = question;
        return this;
    }

    public AnswerRes setUser(UserResDto user) {
        this.user = user;
        return this;
    }

    public AnswerRes setQuestionnaire(QuestionnaireRes questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }

    public AnswerRes setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
