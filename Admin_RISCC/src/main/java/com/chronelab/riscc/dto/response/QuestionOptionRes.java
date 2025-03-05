package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOptionRes {
    private Long id;
    private String title;
    private String value;
    private String researchId;
    private String optionMessage;
    private Boolean notifyUser;//Whether to send email to the use who selected this option as an answer
    private String otherEmail;//Email address, message to be sent if chosen while saving question
    private Boolean notifyOther;//Whether to send email to otherEmail
    private String risccValue;

    public QuestionOptionRes setId(Long id) {
        this.id = id;
        return this;
    }

    public QuestionOptionRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionOptionRes setValue(String value) {
        this.value = value;
        return this;
    }

    public QuestionOptionRes setResearchId(String researchId) {
        this.researchId = researchId;
        return this;
    }

    public QuestionOptionRes setOptionMessage(String optionMessage) {
        this.optionMessage = optionMessage;
        return this;
    }

    public QuestionOptionRes setNotifyUser(Boolean notifyUser) {
        this.notifyUser = notifyUser;
        return this;
    }

    public QuestionOptionRes setOtherEmail(String otherEmail) {
        this.otherEmail = otherEmail;
        return this;
    }

    public QuestionOptionRes setNotifyOther(Boolean notifyOther) {
        this.notifyOther = notifyOther;
        return this;
    }

    public QuestionOptionRes setRisccValue(String risccValue) {
        this.risccValue = risccValue;
        return this;
    }
}
