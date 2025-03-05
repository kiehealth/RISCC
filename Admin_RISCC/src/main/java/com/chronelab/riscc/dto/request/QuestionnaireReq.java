package com.chronelab.riscc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class QuestionnaireReq {
    private Long id;
    private String title;

    private List<QuestionQuestionnaireReq> questionQuestionnaires;
}
