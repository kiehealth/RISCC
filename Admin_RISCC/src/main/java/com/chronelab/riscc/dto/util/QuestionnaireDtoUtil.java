package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.QuestionnaireReq;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuestionnaireDtoUtil implements DtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> {

    private final QuestionQuestionnaireDtoUtil questionQuestionnaireDtoUtil;

    @Autowired
    public QuestionnaireDtoUtil(QuestionQuestionnaireDtoUtil questionQuestionnaireDtoUtil) {
        this.questionQuestionnaireDtoUtil = questionQuestionnaireDtoUtil;
    }

    @Override
    public QuestionnaireEntity reqToEntity(QuestionnaireReq questionnaireReq) {
        return new QuestionnaireEntity()
                .setTitle(questionnaireReq.getTitle());
    }

    @Override
    public QuestionnaireRes entityToRes(QuestionnaireEntity questionnaireEntity) {
        return new QuestionnaireRes()
                .setId(questionnaireEntity.getId())
                .setTitle(questionnaireEntity.getTitle());
    }

    @Override
    public QuestionnaireRes prepRes(QuestionnaireEntity questionnaireEntity) {
        QuestionnaireRes questionnaireResDto = entityToRes(questionnaireEntity);

        if (questionnaireEntity.getQuestionQuestionnaires() != null && questionnaireEntity.getQuestionQuestionnaires().size() > 0) {
            questionnaireResDto.setQuestionQuestionnaires(questionnaireEntity.getQuestionQuestionnaires().stream()
                    .map(questionQuestionnaireDtoUtil::prepRes)
                    .collect(Collectors.toList())
            );
        }
        return questionnaireResDto;
    }

    @Override
    public void setUpdatedValue(QuestionnaireReq questionnaireReq, QuestionnaireEntity questionnaireEntity) {
        if (questionnaireReq != null && questionnaireEntity != null) {
            if (questionnaireReq.getTitle() != null && !questionnaireReq.getTitle().equals(questionnaireEntity.getTitle())) {
                questionnaireEntity.setTitle(questionnaireReq.getTitle());
            }
        }
    }
}
