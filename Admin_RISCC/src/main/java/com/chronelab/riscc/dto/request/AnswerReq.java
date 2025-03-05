package com.chronelab.riscc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AnswerReq {
    private Long id;
    private String answer;
    private String researchId;
    private Long questionId;
    private List<AnswerOptionReq> answerOptions;
    private Long groupQuestionnaireId;
}
