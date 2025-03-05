package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class AnswerOptionReq {
    private Long id;
    private String value;
    private Long questionOptionId;
}
