package com.chronelab.riscc.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GroupReq {
    private Long id;
    private String title;
    private String description;

    private List<Long> userIds;
    private List<GroupQuestionnaireReq> groupQuestionnaires;
}
