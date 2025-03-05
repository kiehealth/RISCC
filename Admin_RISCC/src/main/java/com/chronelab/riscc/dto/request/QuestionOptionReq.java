package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class QuestionOptionReq {
    private Long id;
    private String title;
    private String value;
    private String researchId;
    private String optionMessage;
    private Boolean notifyUser;//Whether to send email to the use who selected this option as an answer
    private String otherEmail;//Email address, message to be sent if chosen while saving question
    private Boolean notifyOther;//Whether to send email to otherEmail
    private String risccValue;
}
