package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackRes {
    private Long id;
    private String title;
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;

    private UserResDto feedbackBy;

    private String runningOS;
    private String phoneModel;

    public FeedbackRes setId(Long id) {
        this.id = id;
        return this;
    }

    public FeedbackRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public FeedbackRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public FeedbackRes setDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public FeedbackRes setFeedbackBy(UserResDto feedbackBy) {
        this.feedbackBy = feedbackBy;
        return this;
    }

    public FeedbackRes setRunningOS(String runningOS) {
        this.runningOS = runningOS;
        return this;
    }

    public FeedbackRes setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
        return this;
    }
}
