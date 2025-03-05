package com.chronelab.riscc.dto.request;

import com.chronelab.riscc.util.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupQuestionnaireReq {
    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDateTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDateTime;

    private String reminderMessage;
    private Integer reminderTimeInterval;

    private Long questionnaireId;
}
