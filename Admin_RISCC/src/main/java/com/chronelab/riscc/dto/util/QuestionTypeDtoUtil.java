package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.QuestionTypeReq;
import com.chronelab.riscc.dto.response.QuestionTypeRes;
import com.chronelab.riscc.entity.QuestionTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionTypeDtoUtil implements DtoUtil<QuestionTypeEntity, QuestionTypeReq, QuestionTypeRes> {

    @Override
    public QuestionTypeEntity reqToEntity(QuestionTypeReq vehicleTypeReq) {
        return new QuestionTypeEntity().setTitle(vehicleTypeReq.getTitle());
    }

    @Override
    public QuestionTypeRes entityToRes(QuestionTypeEntity vehicleTypeEntity) {
        return new QuestionTypeRes().setId(vehicleTypeEntity.getId()).setTitle(vehicleTypeEntity.getTitle());
    }

    @Override
    public QuestionTypeRes prepRes(QuestionTypeEntity vehicleTypeEntity) {
        return entityToRes(vehicleTypeEntity);
    }

    @Override
    public void setUpdatedValue(QuestionTypeReq vehicleTypeReq, QuestionTypeEntity vehicleTypeEntity) {
        if (vehicleTypeReq != null && vehicleTypeEntity != null) {
            if (vehicleTypeReq.getTitle() != null && !vehicleTypeReq.getTitle().equals(vehicleTypeEntity.getTitle())) {
                vehicleTypeEntity.setTitle(vehicleTypeReq.getTitle());
            }
        }
    }
}
