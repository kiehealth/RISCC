package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.dto.response.general.UserResDto;
import lombok.Data;

import java.util.List;

@Data
public class GroupRes {
    private Long id;
    private String title;
    private String description;

    private List<UserResDto> users;
    private List<GroupQuestionnaireRes> groupQuestionnaires;

    public GroupRes setId(Long id) {
        this.id = id;
        return this;
    }

    public GroupRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public GroupRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public GroupRes setUsers(List<UserResDto> users) {
        this.users = users;
        return this;
    }

    public GroupRes setGroupQuestionnaires(List<GroupQuestionnaireRes> groupQuestionnaires) {
        this.groupQuestionnaires = groupQuestionnaires;
        return this;
    }
}
