package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.enums.AppAnalyticsKeyDataType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_app_analytics_key")
@DynamicInsert
@DynamicUpdate
public class AppAnalyticsKeyEntity extends CommonEntity {

    @Column(name = "key_title", nullable = false)
    private String keyTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_analytics_key_data_type", nullable = false, columnDefinition = "ENUM ('DATETIME', 'NUMBER', 'TEXT')")
    private AppAnalyticsKeyDataType appAnalyticsKeyDataType;

    @OneToMany(mappedBy = "appAnalyticsKey")
    private List<AppAnalyticsEntity> appAnalytics;

    public AppAnalyticsKeyEntity setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
        return this;
    }

    public AppAnalyticsKeyEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public AppAnalyticsKeyEntity setAppAnalyticsKeyDataType(AppAnalyticsKeyDataType appAnalyticsKeyDataType) {
        this.appAnalyticsKeyDataType = appAnalyticsKeyDataType;
        return this;
    }

    public AppAnalyticsKeyEntity setAppAnalytics(List<AppAnalyticsEntity> appAnalytics) {
        this.appAnalytics = appAnalytics;
        return this;
    }
}
