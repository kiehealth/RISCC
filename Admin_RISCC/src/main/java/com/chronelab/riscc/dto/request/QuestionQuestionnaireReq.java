package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class QuestionQuestionnaireReq {
    private Long id;
    private Integer displayOrder;
    private Long questionnaireId;
    private Long questionId;
}
