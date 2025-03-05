package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.dto.response.general.RoleResDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.enums.NotificationType;
import com.chronelab.riscc.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRes {
    private Long id;
    private String title;
    private String description;
    private NotificationType notificationType;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    private List<UserResDto> users;
    private List<RoleResDto> roles;
    private List<GroupRes> groups;

    public NotificationRes setId(Long id) {
        this.id = id;
        return this;
    }

    public NotificationRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public NotificationRes setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public NotificationRes setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public NotificationRes setUsers(List<UserResDto> users) {
        this.users = users;
        return this;
    }

    public NotificationRes setRoles(List<RoleResDto> roles) {
        this.roles = roles;
        return this;
    }

    public NotificationRes setGroups(List<GroupRes> groups) {
        this.groups = groups;
        return this;
    }
}
