package com.chronelab.riscc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AnswerReqContainer {
    private Long groupQuestionnaireId;
    private List<AnswerReq> answers;
}
