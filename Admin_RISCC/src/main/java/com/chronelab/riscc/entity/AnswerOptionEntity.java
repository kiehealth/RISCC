package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_answer_option")
@DynamicInsert
@DynamicUpdate
public class AnswerOptionEntity extends CommonEntity {

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @ManyToOne
    @JoinColumn(name = "question_option_id", nullable = false)
    private QuestionOptionEntity questionOption;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private AnswerEntity answer;

    public AnswerOptionEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public AnswerOptionEntity setQuestionOption(QuestionOptionEntity questionOption) {
        this.questionOption = questionOption;
        return this;
    }

    public AnswerOptionEntity setAnswer(AnswerEntity answer) {
        this.answer = answer;
        return this;
    }
}
