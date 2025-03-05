package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.AnswerOptionReq;
import com.chronelab.riscc.dto.response.AnswerOptionRes;
import com.chronelab.riscc.entity.AnswerOptionEntity;
import org.springframework.stereotype.Component;

@Component
public class AnswerOptionDtoUtil implements DtoUtil<AnswerOptionEntity, AnswerOptionReq, AnswerOptionRes> {
    @Override
    public AnswerOptionEntity reqToEntity(AnswerOptionReq answerOptionReq) {
        return new AnswerOptionEntity()
                .setValue(answerOptionReq.getValue());
    }

    @Override
    public AnswerOptionRes entityToRes(AnswerOptionEntity answerOptionEntity) {
        return new AnswerOptionRes()
                .setId(answerOptionEntity.getId())
                .setTitle(answerOptionEntity.getQuestionOption().getTitle())
                .setValue(answerOptionEntity.getValue())
                .setAnswerCode(answerOptionEntity.getQuestionOption().getResearchId())
                .setRisccValue(answerOptionEntity.getQuestionOption().getRisccValue());
    }

    @Override
    public AnswerOptionRes prepRes(AnswerOptionEntity answerOptionEntity) {
        return entityToRes(answerOptionEntity);
    }

    @Override
    public void setUpdatedValue(AnswerOptionReq answerOptionReq, AnswerOptionEntity answerOptionEntity) {

    }
}
