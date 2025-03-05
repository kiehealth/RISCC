package com.chronelab.riscc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class QuestionReq {
    private Long id;
    private String title;
    private String body;

    private Long questionTypeId;
    private List<Long> questionnaireIds;

    private List<QuestionOptionReq> questionOptions;
    private List<QuestionQuestionnaireReq> questionQuestionnaires;
}
