package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.NotificationType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_notification")
@DynamicInsert
@DynamicUpdate
public class NotificationEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, columnDefinition = "ENUM ('BROADCAST', 'ROLE', 'GROUP', 'INDIVIDUAL')")
    private NotificationType notificationType;

    @ManyToMany
    @JoinTable(
            name = "tbl_notification_user",
            joinColumns = @JoinColumn(name = "notification_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false)
    )
    private List<UserEntity> users;

    @ManyToMany
    @JoinTable(
            name = "tbl_notification_role",
            joinColumns = @JoinColumn(name = "notification_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private List<RoleEntity> roles;

    @ManyToMany
    @JoinTable(
            name = "tbl_notification_group",
            joinColumns = @JoinColumn(name = "notification_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "group_id", nullable = false)
    )
    private List<GroupEntity> groups;

    public NotificationEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public NotificationEntity setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public NotificationEntity setUsers(List<UserEntity> users) {
        this.users = users;
        return this;
    }

    public NotificationEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public NotificationEntity setGroups(List<GroupEntity> groups) {
        this.groups = groups;
        return this;
    }
}
