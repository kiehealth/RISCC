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
@Table(name = "tbl_about_us")
@DynamicInsert
@DynamicUpdate
public class AboutUsEntity extends CommonEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    public AboutUsEntity setName(String name) {
        this.name = name;
        return this;
    }

    public AboutUsEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AboutUsEntity setEmail(String email) {
        this.email = email;
        return this;
    }
}
