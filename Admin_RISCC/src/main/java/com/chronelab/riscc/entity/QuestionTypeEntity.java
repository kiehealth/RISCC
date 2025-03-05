package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_question_type")
@DynamicInsert
@DynamicUpdate
public class QuestionTypeEntity extends CommonEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "questionType")
    private List<QuestionEntity> questions;

    public QuestionTypeEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionTypeEntity setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
        return this;
    }
}
