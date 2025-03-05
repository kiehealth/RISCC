package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupQuestionnaireRes {
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startDateTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endDateTime;

    private String reminderMessage;
    private Integer reminderTimeInterval;

    private QuestionnaireRes questionnaire;

    public GroupQuestionnaireRes setId(Long id) {
        this.id = id;
        return this;
    }

    public GroupQuestionnaireRes setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public GroupQuestionnaireRes setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public GroupQuestionnaireRes setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
        return this;
    }

    public GroupQuestionnaireRes setReminderTimeInterval(Integer reminderTimeInterval) {
        this.reminderTimeInterval = reminderTimeInterval;
        return this;
    }

    public GroupQuestionnaireRes setQuestionnaire(QuestionnaireRes questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }
}
