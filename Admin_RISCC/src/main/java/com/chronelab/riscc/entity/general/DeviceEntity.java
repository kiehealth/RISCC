package com.chronelab.riscc.entity.general;

import com.chronelab.riscc.enums.IOSNotificationMode;
import com.chronelab.riscc.enums.Platform;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_device")
@DynamicInsert
@DynamicUpdate
public class DeviceEntity extends CommonEntity {

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", columnDefinition = "ENUM ('ANDROID', 'IOS')")
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(name = "ios_notification_mode", columnDefinition = "ENUM ('SANDBOX', 'DIST')")
    private IOSNotificationMode iosNotificationMode;

    @Column(name = "logged_out")
    private Boolean loggedOut;

    @Column(name = "badge_count")
    private Integer badgeCount;//For push notification counter

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public DeviceEntity setToken(String token) {
        this.token = token;
        return this;
    }

    public DeviceEntity setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public DeviceEntity setIosNotificationMode(IOSNotificationMode iosNotificationMode) {
        this.iosNotificationMode = iosNotificationMode;
        return this;
    }

    public DeviceEntity setLoggedOut(Boolean loggedOut) {
        this.loggedOut = loggedOut;
        return this;
    }

    public DeviceEntity setBadgeCount(Integer badgeCount) {
        this.badgeCount = badgeCount;
        return this;
    }

    public DeviceEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DeviceEntity)) {
            return false;
        }
        DeviceEntity that = (DeviceEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
