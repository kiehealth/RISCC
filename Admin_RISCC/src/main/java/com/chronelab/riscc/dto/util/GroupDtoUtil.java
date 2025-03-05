package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.GroupReq;
import com.chronelab.riscc.dto.response.GroupRes;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupDtoUtil implements DtoUtil<GroupEntity, GroupReq, GroupRes> {

    private final UserDtoUtil userDtoUtil;
    private final GroupQuestionnaireDtoUtil groupQuestionnaireDtoUtil;

    @Autowired
    public GroupDtoUtil(UserDtoUtil userDtoUtil, GroupQuestionnaireDtoUtil groupQuestionnaireDtoUtil) {
        this.userDtoUtil = userDtoUtil;
        this.groupQuestionnaireDtoUtil = groupQuestionnaireDtoUtil;
    }

    @Override
    public GroupEntity reqToEntity(GroupReq groupReq) {
        return new GroupEntity()
                .setTitle(groupReq.getTitle())
                .setDescription(groupReq.getDescription());
    }

    @Override
    public GroupRes entityToRes(GroupEntity groupEntity) {
        return new GroupRes()
                .setId(groupEntity.getId())
                .setTitle(groupEntity.getTitle())
                .setDescription(groupEntity.getDescription());
    }

    @Override
    public GroupRes prepRes(GroupEntity groupEntity) {
        GroupRes groupRes = entityToRes(groupEntity);
        if (groupEntity.getUsers() != null) {
            groupRes.setUsers(groupEntity.getUsers()
                    .stream()
                    .map(userDtoUtil::entityToRes)
                    .collect(Collectors.toList()));
        }
        if (groupEntity.getGroupQuestionnaires() != null) {
            groupRes.setGroupQuestionnaires(groupEntity.getGroupQuestionnaires().stream()
                    .map(groupQuestionnaireDtoUtil::prepRes)
                    .collect(Collectors.toList()));
        }
        return groupRes;
    }

    @Override
    public void setUpdatedValue(GroupReq groupReq, GroupEntity groupEntity) {
        if (groupReq != null && groupEntity != null) {
            if (groupReq.getTitle() != null && !groupReq.getTitle().equals(groupEntity.getTitle())) {
                groupEntity.setTitle(groupReq.getTitle());
            }
            if (groupReq.getDescription() != null &&
                    (groupEntity.getDescription() == null || !groupReq.getDescription().equals(groupEntity.getDescription()))) {
                groupEntity.setDescription(groupReq.getDescription());
            }
        }
    }
}
