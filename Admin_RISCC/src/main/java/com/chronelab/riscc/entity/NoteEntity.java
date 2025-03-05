package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_note")
@DynamicInsert
@DynamicUpdate
public class NoteEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public NoteEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public NoteEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public NoteEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
