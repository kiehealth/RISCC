package com.chronelab.riscc.repo.general;

import com.chronelab.riscc.entity.GroupEntity;
import com.chronelab.riscc.entity.general.DeviceEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepo extends JpaRepository<DeviceEntity, Long> {
    default List<DeviceEntity> findAllByPlatformAndTokenNotNullAndLoggedOutFalse(Platform platform) {
        return findAllByPlatformAndTokenNotNullAndLoggedOutFalseAndTokenNot(platform, "");
    }

    List<DeviceEntity> findAllByPlatformAndTokenNotNullAndLoggedOutFalseAndTokenNot(Platform platform, String token);

    default List<DeviceEntity> findAllByPlatformAndUser_RoleAndTokenNotNullAndLoggedOutFalse(Platform platform, RoleEntity roleEntity) {
        return findAllByPlatformAndUser_RoleAndTokenNotNullAndTokenNotAndLoggedOutFalse(platform, roleEntity, "");
    }

    List<DeviceEntity> findAllByPlatformAndUser_RoleAndTokenNotNullAndTokenNotAndLoggedOutFalse(Platform platform, RoleEntity roleEntity, String token);
/*
    This query is not returning data, can't find the reason. executing the same query in db returns result.
    @Query(value = "SELECT d.token \"token\", d.ios_notification_mode \"iosNotificationMode\" FROM tbl_device d, tbl_user u, tbl_user_group ug " +
            "WHERE d.user_id = u.id " +
            "AND u.id = ug.user_id " +
            "AND d.token IS NOT NULL " +
            "AND d.token != '' " +
            "AND d.platform = :platform " +
            "AND ug.group_id IN (:groupIds)", nativeQuery = true)
    List<DeviceProjection> filterDeviceByPlatformAndGroup(@Param("platform") Platform platform, @Param("groupIds") List<Long> groupIds);*/

    List<DeviceEntity> findAllByPlatformAndUser_GroupsInAndTokenNotNullAndTokenNotAndLoggedOutFalse(Platform platform, List<GroupEntity> groupEntities, String token);
}
