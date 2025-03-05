package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_feedback")
@DynamicInsert
@DynamicUpdate
public class FeedbackEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "running_os")
    private String runningOS;

    @Column(name = "phone_model")
    private String phoneModel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public FeedbackEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public FeedbackEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public FeedbackEntity setRunningOS(String runningOS) {
        this.runningOS = runningOS;
        return this;
    }

    public FeedbackEntity setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
        return this;
    }

    public FeedbackEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
