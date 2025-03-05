package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_setting")
@DynamicInsert
@DynamicUpdate
public class SettingEntity extends CommonEntity {

    @Column(name = "key_title", nullable = false)
    private String keyTitle;

    @Column(name = "key_value", nullable = false)
    private String keyValue;

    public SettingEntity setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
        return this;
    }

    public SettingEntity setKeyValue(String keyValue) {
        this.keyValue = keyValue;
        return this;
    }
}
