package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_app_version")
public class AppVersionEntity extends CommonEntity {

    @Column(name = "family", nullable = false)
    private String family;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "force_update", nullable = false)
    private boolean forceUpdate;

    private String url;

    public AppVersionEntity setFamily(String family) {
        this.family = family;
        return this;
    }

    public AppVersionEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public AppVersionEntity setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        return this;
    }

    public AppVersionEntity setUrl(String url) {
        this.url = url;
        return this;
    }
}
