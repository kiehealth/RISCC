package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.AppAnalyticsKeyReq;
import com.chronelab.riscc.dto.response.AppAnalyticsKeyRes;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import org.springframework.stereotype.Component;

@Component
public class AppAnalyticsKeyDtoUtil implements DtoUtil<AppAnalyticsKeyEntity, AppAnalyticsKeyReq, AppAnalyticsKeyRes> {
    @Override
    public AppAnalyticsKeyEntity reqToEntity(AppAnalyticsKeyReq appAnalyticsKeyReq) {
        return new AppAnalyticsKeyEntity()
                .setKeyTitle(appAnalyticsKeyReq.getTitle())
                .setDescription(appAnalyticsKeyReq.getDescription())
                .setAppAnalyticsKeyDataType(appAnalyticsKeyReq.getAppAnalyticsKeyDataType());
    }

    @Override
    public AppAnalyticsKeyRes entityToRes(AppAnalyticsKeyEntity appAnalyticsKeyEntity) {
        return new AppAnalyticsKeyRes()
                .setId(appAnalyticsKeyEntity.getId())
                .setTitle(appAnalyticsKeyEntity.getKeyTitle())
                .setDescription(appAnalyticsKeyEntity.getDescription())
                .setAppAnalyticsKeyDataType(appAnalyticsKeyEntity.getAppAnalyticsKeyDataType());
    }

    @Override
    public AppAnalyticsKeyRes prepRes(AppAnalyticsKeyEntity appAnalyticsKeyEntity) {
        return entityToRes(appAnalyticsKeyEntity);
    }

    @Override
    public void setUpdatedValue(AppAnalyticsKeyReq appAnalyticsKeyReq, AppAnalyticsKeyEntity appAnalyticsKeyEntity) {
        if (appAnalyticsKeyReq != null && appAnalyticsKeyEntity != null) {
            if (appAnalyticsKeyReq.getTitle() != null && !appAnalyticsKeyReq.getTitle().equals(appAnalyticsKeyEntity.getKeyTitle())) {
                appAnalyticsKeyEntity.setKeyTitle(appAnalyticsKeyReq.getTitle());
            }
            if (appAnalyticsKeyReq.getDescription() != null &&
                    (appAnalyticsKeyEntity.getDescription() == null || !appAnalyticsKeyReq.getDescription().equals(appAnalyticsKeyEntity.getDescription()))) {
                appAnalyticsKeyEntity.setDescription(appAnalyticsKeyReq.getDescription());
            }
            if (appAnalyticsKeyReq.getAppAnalyticsKeyDataType() != null && !appAnalyticsKeyReq.getAppAnalyticsKeyDataType().equals(appAnalyticsKeyEntity.getAppAnalyticsKeyDataType())) {
                appAnalyticsKeyEntity.setAppAnalyticsKeyDataType(appAnalyticsKeyReq.getAppAnalyticsKeyDataType());
            }
        }
    }
}
