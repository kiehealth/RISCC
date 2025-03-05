package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.QuestionOptionReq;
import com.chronelab.riscc.dto.response.QuestionOptionRes;
import com.chronelab.riscc.entity.QuestionOptionEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionOptionDtoUtil implements DtoUtil<QuestionOptionEntity, QuestionOptionReq, QuestionOptionRes> {
    @Override
    public QuestionOptionEntity reqToEntity(QuestionOptionReq questionOptionReq) {
        return new QuestionOptionEntity()
                .setTitle(questionOptionReq.getTitle())
                .setValue(questionOptionReq.getValue())
                .setResearchId(questionOptionReq.getResearchId())
                .setOptionMessage(questionOptionReq.getOptionMessage())
                .setRisccValue(questionOptionReq.getRisccValue())
                .setNotifyUser(questionOptionReq.getNotifyUser())
                .setOtherEmail(questionOptionReq.getOtherEmail())
                .setNotifyOther(questionOptionReq.getNotifyOther());
    }

    @Override
    public QuestionOptionRes entityToRes(QuestionOptionEntity questionOptionEntity) {
        return new QuestionOptionRes()
                .setId(questionOptionEntity.getId())
                .setTitle(questionOptionEntity.getTitle())
                .setValue(questionOptionEntity.getValue())
                .setResearchId(questionOptionEntity.getResearchId())
                .setOptionMessage(questionOptionEntity.getOptionMessage())
                .setNotifyUser(questionOptionEntity.getNotifyUser())
                .setOtherEmail(questionOptionEntity.getOtherEmail())
                .setNotifyOther(questionOptionEntity.getNotifyOther())
                .setRisccValue(questionOptionEntity.getRisccValue());
    }

    @Override
    public QuestionOptionRes prepRes(QuestionOptionEntity questionOptionEntity) {
        return entityToRes(questionOptionEntity);
    }

    @Override
    public void setUpdatedValue(QuestionOptionReq questionOptionReq, QuestionOptionEntity questionOptionEntity) {
        if (questionOptionReq != null && questionOptionEntity != null) {
            if (questionOptionReq.getTitle() != null && !questionOptionReq.getTitle().equals(questionOptionEntity.getTitle())) {
                questionOptionEntity.setTitle(questionOptionReq.getTitle());
            }
            if (questionOptionReq.getValue() != null && !questionOptionReq.getValue().equals(questionOptionEntity.getValue())) {
                questionOptionEntity.setValue(questionOptionReq.getValue());
            }
            if (questionOptionReq.getResearchId() != null
                    && (questionOptionEntity.getResearchId() == null || !questionOptionReq.getResearchId().equals(questionOptionEntity.getResearchId()))) {
                questionOptionEntity.setResearchId(questionOptionReq.getResearchId());
            }
            if (questionOptionReq.getOptionMessage() != null
                    && (questionOptionEntity.getOptionMessage() == null || !questionOptionReq.getOptionMessage().equals(questionOptionEntity.getOptionMessage()))) {
                questionOptionEntity.setOptionMessage(questionOptionReq.getOptionMessage());
            }
            if (questionOptionReq.getNotifyUser() != null
                    && (questionOptionEntity.getNotifyUser() == null || !questionOptionReq.getNotifyUser().equals(questionOptionEntity.getNotifyUser()))) {
                questionOptionEntity.setNotifyUser(questionOptionReq.getNotifyUser());
            }
            if (questionOptionReq.getOtherEmail() != null
                    && (questionOptionEntity.getOtherEmail() == null || !questionOptionReq.getOtherEmail().equals(questionOptionEntity.getOtherEmail()))) {
                questionOptionEntity.setOtherEmail(questionOptionReq.getOtherEmail());
            }
            if (questionOptionReq.getNotifyOther() != null
                    && (questionOptionEntity.getNotifyOther() == null || !questionOptionReq.getNotifyOther().equals(questionOptionEntity.getNotifyOther()))) {
                questionOptionEntity.setNotifyOther(questionOptionReq.getNotifyOther());
            }
            if(questionOptionReq.getRisccValue() != null
                    && (questionOptionEntity.getRisccValue() == null || !questionOptionReq.getRisccValue().equals(questionOptionEntity.getRisccValue()))){
                questionOptionEntity.setRisccValue(questionOptionReq.getRisccValue());
            }
        }
    }
}
