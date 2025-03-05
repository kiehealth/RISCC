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
@Table(name = "tbl_resource_file")
@DynamicInsert
@DynamicUpdate
public class ResourceFileEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url", nullable = false)
    private String url;

    public ResourceFileEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public ResourceFileEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public ResourceFileEntity setUrl(String url) {
        this.url = url;
        return this;
    }
}
