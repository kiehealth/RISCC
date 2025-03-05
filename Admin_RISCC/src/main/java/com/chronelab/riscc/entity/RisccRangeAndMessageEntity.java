package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_riscc_range")
@DynamicInsert
@DynamicUpdate
public class RisccRangeAndMessageEntity extends CommonEntity {

    @Column(name = "to_value", nullable = false)
    private String to_value;

    @Column(name = "from_value", nullable = false)
    private String from_value;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "more_info", columnDefinition = "LONGTEXT")
    private String moreInfo;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id", nullable = false)
    private QuestionnaireEntity questionnaire;

    public RisccRangeAndMessageEntity setTo_value(String to_riscc_value) {
        this.to_value = to_riscc_value;
        return this;
    }

    public RisccRangeAndMessageEntity setFrom_value(String from_riscc_value) {
        this.from_value = from_riscc_value;
        return this;
    }

    public RisccRangeAndMessageEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public RisccRangeAndMessageEntity setQuestionnaire(QuestionnaireEntity questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }

    public RisccRangeAndMessageEntity setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
        return this;
    }
}
