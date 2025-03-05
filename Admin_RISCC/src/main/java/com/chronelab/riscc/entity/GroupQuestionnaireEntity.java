package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_group_questionnaire",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"group_id", "questionnaire_id"})
        }
)
@DynamicInsert
@DynamicUpdate
public class GroupQuestionnaireEntity extends CommonEntity {

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "reminder_message")
    private String reminderMessage;

    @Column(name = "reminder_time_interval")
    private Integer reminderTimeInterval;//In minutes

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private QuestionnaireEntity questionnaire;

    public GroupQuestionnaireEntity setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public GroupQuestionnaireEntity setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public GroupQuestionnaireEntity setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public GroupQuestionnaireEntity setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
        return this;
    }

    public GroupQuestionnaireEntity setReminderTimeInterval(Integer reminderTimeInterval) {
        this.reminderTimeInterval = reminderTimeInterval;
        return this;
    }

    public GroupQuestionnaireEntity setGroup(GroupEntity group) {
        this.group = group;
        return this;
    }

    public GroupQuestionnaireEntity setQuestionnaire(QuestionnaireEntity questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }
}
