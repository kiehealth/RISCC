package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerOptionRes {
    private Long id;
    private String title;
    private String value;
    private String answerCode;
    private String risccValue;

    public AnswerOptionRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AnswerOptionRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public AnswerOptionRes setValue(String value) {
        this.value = value;
        return this;
    }

    public AnswerOptionRes setAnswerCode(String answerCode) {
        this.answerCode = answerCode;
        return this;
    }

    public AnswerOptionRes setRisccValue(String risccValue) {
        this.risccValue = risccValue;
        return this;
    }
}
