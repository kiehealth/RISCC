package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.QuestionReq;
import com.chronelab.riscc.dto.response.QuestionRes;
import com.chronelab.riscc.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuestionDtoUtil implements DtoUtil<QuestionEntity, QuestionReq, QuestionRes> {

    private final QuestionTypeDtoUtil questionTypeDtoUtil;
    private final QuestionQuestionnaireDtoUtil questionQuestionnaireDtoUtil;
    private final QuestionOptionDtoUtil questionOptionDtoUtil;

    @Autowired
    public QuestionDtoUtil(QuestionTypeDtoUtil questionTypeDtoUtil, QuestionQuestionnaireDtoUtil questionQuestionnaireDtoUtil, QuestionOptionDtoUtil questionOptionDtoUtil) {
        this.questionTypeDtoUtil = questionTypeDtoUtil;
        this.questionQuestionnaireDtoUtil = questionQuestionnaireDtoUtil;
        this.questionOptionDtoUtil = questionOptionDtoUtil;
    }

    @Override
    public QuestionEntity reqToEntity(QuestionReq questionReq) {
        return new QuestionEntity()
                .setTitle(questionReq.getTitle())
                .setBody(questionReq.getBody());
    }

    @Override
    public QuestionRes entityToRes(QuestionEntity questionEntity) {
        return new QuestionRes()
                .setId(questionEntity.getId())
                .setTitle(questionEntity.getTitle())
                .setBody(questionEntity.getBody());
    }

    @Override
    public QuestionRes prepRes(QuestionEntity questionEntity) {
        QuestionRes questionResDto = entityToRes(questionEntity);

        questionResDto.setQuestionType(questionTypeDtoUtil.entityToRes(questionEntity.getQuestionType()));

        if (questionEntity.getQuestionQuestionnaires() != null) {
            questionResDto.setQuestionQuestionnaires(questionEntity.getQuestionQuestionnaires().stream()
                    .map(questionQuestionnaireDtoUtil::prepRes)
                    .collect(Collectors.toList())
            );
        }

        if (questionEntity.getQuestionOptions() != null) {
            questionResDto.setQuestionOptions(questionEntity.getQuestionOptions().stream()
                    .map(questionOptionDtoUtil::entityToRes)
                    .collect(Collectors.toList())
            );
        }

        return questionResDto;
    }

    @Override
    public void setUpdatedValue(QuestionReq questionReq, QuestionEntity questionEntity) {
        if (questionReq != null && questionEntity != null) {
            if (questionReq.getTitle() != null
                    && !questionReq.getTitle().isEmpty()
                    && !questionReq.getTitle().equals(questionEntity.getTitle())) {
                questionEntity.setTitle(questionReq.getTitle());
            }
            if (questionReq.getBody() != null
                    && !questionReq.getBody().equals(questionEntity.getBody())) {
                questionEntity.setBody(questionReq.getBody());
            }
        }
    }
}
