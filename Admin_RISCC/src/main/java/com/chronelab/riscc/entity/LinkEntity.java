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
@Table(name = "tbl_link")
@DynamicInsert
@DynamicUpdate
public class LinkEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "contact_number")
    private String contactNumber;

    public LinkEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public LinkEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LinkEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public LinkEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public LinkEntity setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }
}
