package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.NotificationReq;
import com.chronelab.riscc.dto.response.NotificationRes;
import com.chronelab.riscc.dto.util.general.RoleDtoUtil;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.NotificationEntity;
import com.chronelab.riscc.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NotificationDtoUtil implements DtoUtil<NotificationEntity, NotificationReq, NotificationRes> {

    private final UserDtoUtil userDtoUtil;
    private final RoleDtoUtil roleDtoUtil;
    private final GroupDtoUtil groupDtoUtil;

    @Autowired
    public NotificationDtoUtil(UserDtoUtil userDtoUtil, RoleDtoUtil roleDtoUtil, GroupDtoUtil groupDtoUtil) {
        this.userDtoUtil = userDtoUtil;
        this.roleDtoUtil = roleDtoUtil;
        this.groupDtoUtil = groupDtoUtil;
    }

    @Override
    public NotificationEntity reqToEntity(NotificationReq notificationReq) {
        return new NotificationEntity()
                .setTitle(notificationReq.getTitle())
                .setDescription(notificationReq.getDescription())
                .setNotificationType(notificationReq.getNotificationType());
    }

    @Override
    public NotificationRes entityToRes(NotificationEntity notificationEntity) {
        return new NotificationRes()
                .setId(notificationEntity.getId())
                .setTitle(notificationEntity.getTitle())
                .setDescription(notificationEntity.getDescription())
                .setNotificationType(notificationEntity.getNotificationType())
                .setDateTime(notificationEntity.getCreatedDate());
    }

    @Override
    public NotificationRes prepRes(NotificationEntity notificationEntity) {
        NotificationRes notificationResDto = entityToRes(notificationEntity);
        if (notificationEntity.getNotificationType().equals(NotificationType.INDIVIDUAL) && notificationEntity.getUsers() != null) {
            notificationResDto.setUsers(
                    notificationEntity.getUsers().stream()
                            .map(userDtoUtil::entityToRes)
                            .collect(Collectors.toList())
            );
        }
        if (notificationEntity.getNotificationType().equals(NotificationType.ROLE) && notificationEntity.getRoles() != null) {
            notificationResDto.setRoles(
                    notificationEntity.getRoles().stream()
                            .map(roleDtoUtil::entityToRes)
                            .collect(Collectors.toList())
            );
        }
        if (notificationEntity.getNotificationType().equals(NotificationType.GROUP) && notificationEntity.getGroups() != null) {
            notificationResDto.setGroups(
                    notificationEntity.getGroups().stream()
                            .map(groupDtoUtil::entityToRes)
                            .collect(Collectors.toList())
            );
        }
        return notificationResDto;
    }

    @Override
    public void setUpdatedValue(NotificationReq notificationReq, NotificationEntity notificationEntity) {

    }
}
