package com.chronelab.riscc.dto.response;

import lombok.Data;

@Data
public class RisccRangeRes {
    private Long id;
    private String fromValue;
    private String toValue;
    private String message;
    private String moreInfo;
    private QuestionnaireRes questionnaireRes;

    public RisccRangeRes setId(Long id) {
        this.id = id;
        return this;
    }

    public RisccRangeRes setFromValue(String fromValue) {
        this.fromValue = fromValue;
        return this;
    }

    public RisccRangeRes setQuestionnaireRes(QuestionnaireRes questionnaireRes) {
        this.questionnaireRes = questionnaireRes;
        return this;
    }

    public RisccRangeRes setFormValue(String fromValue) {
        this.fromValue = fromValue;
        return this;
    }

    public RisccRangeRes setToValue(String toValue) {
        this.toValue = toValue;
        return this;
    }

    public RisccRangeRes setMessage(String message) {
        this.message = message;
        return this;
    }

    public RisccRangeRes setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }
}
