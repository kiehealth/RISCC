package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.MultiLangMessageReq;
import com.chronelab.riscc.dto.response.MultiLangMessageRes;
import com.chronelab.riscc.entity.MultiLangMessageEntity;
import org.springframework.stereotype.Component;

@Component
public class MultiLangMessageDtoUtil implements DtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> {

    @Override
    public MultiLangMessageEntity reqToEntity(MultiLangMessageReq multiLangMessageReq) {
        return new MultiLangMessageEntity()
                .setEnglish(multiLangMessageReq.getEnglish())
                .setSwedish(multiLangMessageReq.getSwedish())
                .setSpanish(multiLangMessageReq.getSpanish());
    }

    @Override
    public MultiLangMessageRes entityToRes(MultiLangMessageEntity multiLangMessageEntity) {
        return new MultiLangMessageRes()
                .setId(multiLangMessageEntity.getId())
                .setCode(multiLangMessageEntity.getCode())
                .setEnglish(multiLangMessageEntity.getEnglish())
                .setSwedish(multiLangMessageEntity.getSwedish())
                .setSpanish(multiLangMessageEntity.getSpanish());
    }

    @Override
    public MultiLangMessageRes prepRes(MultiLangMessageEntity multiLangMessageEntity) {
        return entityToRes(multiLangMessageEntity);
    }

    @Override
    public void setUpdatedValue(MultiLangMessageReq multiLangMessageReq, MultiLangMessageEntity multiLangMessageEntity) {
        if (multiLangMessageReq != null && multiLangMessageEntity != null) {
            /*if (multiLangMessageReq.getEnglish() != null && !multiLangMessageReq.getEnglish().equals(multiLangMessageEntity.getEnglish())) {
                multiLangMessageEntity.setEnglish(multiLangMessageReq.getEnglish());
            }*/
            if (multiLangMessageReq.getSwedish() != null &&
                    (multiLangMessageEntity.getSwedish() == null || !multiLangMessageReq.getSwedish().equals(multiLangMessageEntity.getSwedish()))) {
                multiLangMessageEntity.setSwedish(multiLangMessageReq.getSwedish());
            }
            if (multiLangMessageReq.getSpanish() != null &&
                    (multiLangMessageEntity.getSpanish() == null || !multiLangMessageReq.getSpanish().equals(multiLangMessageEntity.getSpanish()))) {
                multiLangMessageEntity.setSpanish(multiLangMessageReq.getSpanish());
            }
        }
    }
}
