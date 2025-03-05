package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.QuestionnaireReq;
import com.chronelab.riscc.dto.request.RisccRangeReq;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.dto.response.RisccRangeRes;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import com.chronelab.riscc.entity.RisccRangeAndMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RisccRangeDtoUtil implements DtoUtil<RisccRangeAndMessageEntity, RisccRangeReq, RisccRangeRes> {

    private final DtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> questionnaireDtoUtil;

    @Autowired
    public RisccRangeDtoUtil(DtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> questionnaireDtoUtil) {
        this.questionnaireDtoUtil = questionnaireDtoUtil;
    }

    @Override
    public RisccRangeAndMessageEntity reqToEntity(RisccRangeReq risccRangeReq) {
        return new RisccRangeAndMessageEntity()
                .setTo_value(risccRangeReq.getToValue())
                .setFrom_value(risccRangeReq.getFromValue())
                .setMessage(risccRangeReq.getMessage())
                .setMoreInfo(risccRangeReq.getMoreInfo());
    }

    @Override
    public RisccRangeRes entityToRes(RisccRangeAndMessageEntity risccRangeAndMessageEntity) {
        return new RisccRangeRes()
                .setId(risccRangeAndMessageEntity.getId())
                .setFormValue(risccRangeAndMessageEntity.getFrom_value())
                .setToValue(risccRangeAndMessageEntity.getTo_value())
                .setMessage(risccRangeAndMessageEntity.getMessage())
                .setMoreInfo(risccRangeAndMessageEntity.getMoreInfo());
    }

    @Override
    public RisccRangeRes prepRes(RisccRangeAndMessageEntity risccRangeAndMessageEntity) {
        RisccRangeRes risccRangeRes = entityToRes(risccRangeAndMessageEntity);
        QuestionnaireRes questionnaireRes = questionnaireDtoUtil.entityToRes(risccRangeAndMessageEntity.getQuestionnaire());
        risccRangeRes.setQuestionnaireRes(questionnaireRes);
        return risccRangeRes;
    }

    @Override
    public void setUpdatedValue(RisccRangeReq risccRangeReq, RisccRangeAndMessageEntity risccRangeAndMessageEntity) {

    }
}
