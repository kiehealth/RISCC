package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.FeedbackReq;
import com.chronelab.riscc.dto.response.FeedbackRes;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.FeedbackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackDtoUtil implements DtoUtil<FeedbackEntity, FeedbackReq, FeedbackRes> {

    private final UserDtoUtil userDtoUtil;

    @Autowired
    public FeedbackDtoUtil(UserDtoUtil userDtoUtil) {
        this.userDtoUtil = userDtoUtil;
    }

    @Override
    public FeedbackEntity reqToEntity(FeedbackReq feedbackReq) {
        return new FeedbackEntity()
                .setTitle(feedbackReq.getTitle())
                .setDescription(feedbackReq.getDescription())
                .setRunningOS(feedbackReq.getRunningOS())
                .setPhoneModel(feedbackReq.getPhoneModel());
    }

    @Override
    public FeedbackRes entityToRes(FeedbackEntity feedbackEntity) {
        return new FeedbackRes()
                .setId(feedbackEntity.getId())
                .setTitle(feedbackEntity.getTitle())
                .setDescription(feedbackEntity.getDescription())
                .setDate(feedbackEntity.getCreatedDate())
                .setRunningOS(feedbackEntity.getRunningOS())
                .setPhoneModel(feedbackEntity.getPhoneModel());
    }

    @Override
    public FeedbackRes prepRes(FeedbackEntity feedbackEntity) {
        FeedbackRes feedbackResDto = entityToRes(feedbackEntity);
        feedbackResDto.setFeedbackBy(userDtoUtil.entityToRes(feedbackEntity.getUser()));

        return feedbackResDto;
    }

    @Override
    public void setUpdatedValue(FeedbackReq feedbackReq, FeedbackEntity feedbackEntity) {

    }
}
