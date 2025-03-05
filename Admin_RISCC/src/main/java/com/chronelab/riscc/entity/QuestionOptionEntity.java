package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_question_option")
@DynamicInsert
@DynamicUpdate
public class QuestionOptionEntity extends CommonEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "value")
    private String value;

    @Column(name = "research_id")
    private String researchId;

    @Column(name = "riscc_value")
    private String  risccValue;

    @Column(name = "option_message")
    private String optionMessage;//Message to be sent if this option is selected as an answer

    @Column(name = "notify_user")
    private Boolean notifyUser;//Whether to send email to the use who selected this option as an answer

    @Column(name = "other_email")
    private String otherEmail;//Email address, message to be sent if chosen while saving question

    @Column(name = "notify_other")
    private Boolean notifyOther;//Whether to send email to otherEmail

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @OneToMany(mappedBy = "questionOption")
    private List<AnswerOptionEntity> answerOptions;

    public QuestionOptionEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionOptionEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public QuestionOptionEntity setResearchId(String researchId) {
        this.researchId = researchId;
        return this;
    }

    public QuestionOptionEntity setQuestion(QuestionEntity question) {
        this.question = question;
        return this;
    }

    public QuestionOptionEntity setAnswerOptions(List<AnswerOptionEntity> answerOptions) {
        this.answerOptions = answerOptions;
        return this;
    }

    public QuestionOptionEntity setOptionMessage(String optionMessage) {
        this.optionMessage = optionMessage;
        return this;
    }

    public QuestionOptionEntity setNotifyUser(Boolean notifyUser) {
        this.notifyUser = notifyUser;
        return this;
    }

    public QuestionOptionEntity setOtherEmail(String otherEmail) {
        this.otherEmail = otherEmail;
        return this;
    }

    public QuestionOptionEntity setNotifyOther(Boolean notifyOther) {
        this.notifyOther = notifyOther;
        return this;
    }

    public QuestionOptionEntity setRisccValue(String risccValue) {
        this.risccValue = risccValue;
        return this;
    }
}
