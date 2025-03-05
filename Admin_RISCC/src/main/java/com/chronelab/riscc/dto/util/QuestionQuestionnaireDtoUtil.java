package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.response.QuestionQuestionnaireRes;
import com.chronelab.riscc.dto.response.QuestionRes;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.entity.QuestionQuestionnaireEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class QuestionQuestionnaireDtoUtil {

    private final QuestionDtoUtil questionDtoUtil;
    private final QuestionnaireDtoUtil questionnaireDtoUtil;

    @Autowired
    public QuestionQuestionnaireDtoUtil(@Lazy QuestionDtoUtil questionDtoUtil, @Lazy QuestionnaireDtoUtil questionnaireDtoUtil) {
        this.questionDtoUtil = questionDtoUtil;
        this.questionnaireDtoUtil = questionnaireDtoUtil;
    }

    public QuestionQuestionnaireRes prepRes(QuestionQuestionnaireEntity questionQuestionnaireEntity) {
        QuestionRes questionResDto = questionDtoUtil.entityToRes(questionQuestionnaireEntity.getQuestion());
        QuestionnaireRes questionnaireRes = questionnaireDtoUtil.entityToRes(questionQuestionnaireEntity.getQuestionnaire());
        return new QuestionQuestionnaireRes()
                .setId(questionQuestionnaireEntity.getId())
                .setDisplayOrder(questionQuestionnaireEntity.getDisplayOrder())
                .setQuestion(questionResDto)
                .setQuestionnaire(questionnaireRes);
    }
}
