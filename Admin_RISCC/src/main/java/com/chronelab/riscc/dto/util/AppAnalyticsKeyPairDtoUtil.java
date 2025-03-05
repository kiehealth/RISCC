package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.AppAnalyticsKeyPairReq;
import com.chronelab.riscc.dto.response.AppAnalyticsKeyPairRes;
import com.chronelab.riscc.entity.AppAnalyticsKeyPairEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppAnalyticsKeyPairDtoUtil implements DtoUtil<AppAnalyticsKeyPairEntity, AppAnalyticsKeyPairReq, AppAnalyticsKeyPairRes> {

    private AppAnalyticsKeyDtoUtil appAnalyticsKeyDtoUtil;

    @Autowired
    public AppAnalyticsKeyPairDtoUtil(AppAnalyticsKeyDtoUtil appAnalyticsKeyDtoUtil) {
        this.appAnalyticsKeyDtoUtil = appAnalyticsKeyDtoUtil;
    }

    @Override
    public AppAnalyticsKeyPairEntity reqToEntity(AppAnalyticsKeyPairReq appAnalyticsKeyPairReq) {
        return null;
    }

    @Override
    public AppAnalyticsKeyPairRes entityToRes(AppAnalyticsKeyPairEntity appAnalyticsKeyPairEntity) {
        return new AppAnalyticsKeyPairRes()
                .setId(appAnalyticsKeyPairEntity.getId());
    }

    @Override
    public AppAnalyticsKeyPairRes prepRes(AppAnalyticsKeyPairEntity appAnalyticsKeyPairEntity) {
        AppAnalyticsKeyPairRes appAnalyticsKeyPairRes = entityToRes(appAnalyticsKeyPairEntity);
        appAnalyticsKeyPairRes.setAppAnalyticsKeyOne(appAnalyticsKeyDtoUtil.entityToRes(appAnalyticsKeyPairEntity.getKeyOne()));
        if (appAnalyticsKeyPairEntity.getKeyTwo() != null) {
            appAnalyticsKeyPairRes.setAppAnalyticsKeyTwo(appAnalyticsKeyDtoUtil.entityToRes(appAnalyticsKeyPairEntity.getKeyTwo()));
        }
        return appAnalyticsKeyPairRes;
    }

    @Override
    public void setUpdatedValue(AppAnalyticsKeyPairReq appAnalyticsKeyPairReq, AppAnalyticsKeyPairEntity appAnalyticsKeyPairEntity) {

    }
}
