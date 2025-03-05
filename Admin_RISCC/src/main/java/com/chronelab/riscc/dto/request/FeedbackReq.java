package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class FeedbackReq {
    private Long id;
    private String title;
    private String description;
    private String runningOS;
    private String phoneModel;
}
