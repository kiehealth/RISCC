package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_app_analytics_key_pair",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"key_one", "key_two"})
        })
@DynamicInsert
@DynamicUpdate
public class AppAnalyticsKeyPairEntity extends CommonEntity {

    @OneToOne
    @JoinColumn(name = "key_one", nullable = false)
    private AppAnalyticsKeyEntity keyOne;

    @OneToOne
    @JoinColumn(name = "key_two")
    private AppAnalyticsKeyEntity keyTwo;

    public AppAnalyticsKeyPairEntity setKeyOne(AppAnalyticsKeyEntity keyOne) {
        this.keyOne = keyOne;
        return this;
    }

    public AppAnalyticsKeyPairEntity setKeyTwo(AppAnalyticsKeyEntity keyTwo) {
        this.keyTwo = keyTwo;
        return this;
    }
}
