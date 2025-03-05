package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_app_analytics")
@DynamicInsert
@DynamicUpdate
public class AppAnalyticsEntity extends CommonEntity {

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "key_value_int")
    private Integer keyValueInt;

    @Column(name = "key_value_date_time")
    private LocalDateTime keyValueDateTime;

    @Column(name = "key_value_text")
    private String keyValueText;

    @ManyToOne
    @JoinColumn(name = "app_analytics_key_id", nullable = false)
    private AppAnalyticsKeyEntity appAnalyticsKey;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public AppAnalyticsEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public AppAnalyticsEntity setKeyValueInt(Integer keyValueInt) {
        this.keyValueInt = keyValueInt;
        return this;
    }

    public AppAnalyticsEntity setKeyValueDateTime(LocalDateTime keyValueDateTime) {
        this.keyValueDateTime = keyValueDateTime;
        return this;
    }

    public AppAnalyticsEntity setKeyValueText(String keyValueText) {
        this.keyValueText = keyValueText;
        return this;
    }

    public AppAnalyticsEntity setAppAnalyticsKey(AppAnalyticsKeyEntity appAnalyticsKey) {
        this.appAnalyticsKey = appAnalyticsKey;
        return this;
    }

    public AppAnalyticsEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
