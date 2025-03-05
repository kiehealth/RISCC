package com.chronelab.riscc.repo;

import com.chronelab.riscc.entity.NotificationEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findAllByUsersInOrNotificationTypeInOrRolesInAndCreatedDateAfter(List<UserEntity> userEntities, List<NotificationType> notificationTypes, List<RoleEntity> roles, LocalDateTime localDateTime, Pageable pageable);

    List<NotificationEntity> findAllByUsersInOrNotificationTypeInOrRolesInAndCreatedDateAfter(List<UserEntity> userEntities, List<NotificationType> notificationTypes, List<RoleEntity> roles, LocalDateTime localDateTime, Sort sort);

    Page<NotificationEntity> findAllByNotificationType(NotificationType notificationType, Pageable pageable);

    List<NotificationEntity> findAllByNotificationType(NotificationType notificationType, Sort sort);

    Page<NotificationEntity> findAllByNotificationTypeAndRolesIn(NotificationType notificationType, List<RoleEntity> roleEntities, Pageable pageable);

    List<NotificationEntity> findAllByNotificationTypeAndRolesIn(NotificationType notificationType, List<RoleEntity> roleEntities, Sort sort);
}
