package com.chronelab.riscc.entity;

import com.chronelab.riscc.entity.general.CommonEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_questionnaire")
@DynamicInsert
@DynamicUpdate
public class QuestionnaireEntity extends CommonEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "questionnaire", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<QuestionQuestionnaireEntity> questionQuestionnaires;

    public QuestionnaireEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public QuestionnaireEntity setQuestionQuestionnaires(List<QuestionQuestionnaireEntity> questionQuestionnaires) {
        this.questionQuestionnaires = questionQuestionnaires;
        return this;
    }
}
