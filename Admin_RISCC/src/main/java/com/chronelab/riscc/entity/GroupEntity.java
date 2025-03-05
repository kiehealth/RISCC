package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_group",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title"})
        }
)
@DynamicInsert
@DynamicUpdate
public class GroupEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_user_group",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"group_id", "user_id"})
            },
            joinColumns = {@JoinColumn(name = "group_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false)}
    )
    private List<UserEntity> users;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GroupQuestionnaireEntity> groupQuestionnaires;

    public GroupEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public GroupEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public GroupEntity setUsers(List<UserEntity> users) {
        this.users = users;
        return this;
    }

    public GroupEntity setGroupQuestionnaires(List<GroupQuestionnaireEntity> groupQuestionnaires) {
        this.groupQuestionnaires = groupQuestionnaires;
        return this;
    }
}
