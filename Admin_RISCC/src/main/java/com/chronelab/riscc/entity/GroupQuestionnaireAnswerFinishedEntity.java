package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_group_questionnaire_answer_finished")
@DynamicInsert
@DynamicUpdate
public class GroupQuestionnaireAnswerFinishedEntity extends CommonEntity {

    @Column(name = "finished_date_time", nullable = false)
    private LocalDateTime finishedDateTime;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;//Start date time of group questionnaire when answer finished action was done

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;//End date time of group questionnaire when answer finished action was done

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "group_questionnaire_id", nullable = false)
    private GroupQuestionnaireEntity groupQuestionnaire;

    public GroupQuestionnaireAnswerFinishedEntity setFinishedDateTime(LocalDateTime finishedDateTime) {
        this.finishedDateTime = finishedDateTime;
        return this;
    }

    public GroupQuestionnaireAnswerFinishedEntity setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public GroupQuestionnaireAnswerFinishedEntity setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public GroupQuestionnaireAnswerFinishedEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public GroupQuestionnaireAnswerFinishedEntity setGroupQuestionnaire(GroupQuestionnaireEntity groupQuestionnaire) {
        this.groupQuestionnaire = groupQuestionnaire;
        return this;
    }
}
