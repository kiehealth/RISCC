package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.AppAnalyticsReq;
import com.chronelab.riscc.dto.response.AppAnalyticsRes;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.AppAnalyticsEntity;
import com.chronelab.riscc.enums.AppAnalyticsKeyDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AppAnalyticsDtoUtil implements DtoUtil<AppAnalyticsEntity, AppAnalyticsReq, AppAnalyticsRes> {

    private final UserDtoUtil userDtoUtil;
    private final AppAnalyticsKeyDtoUtil appAnalyticsKeyDtoUtil;

    @Autowired
    public AppAnalyticsDtoUtil(UserDtoUtil userDtoUtil, AppAnalyticsKeyDtoUtil appAnalyticsKeyDtoUtil) {
        this.userDtoUtil = userDtoUtil;
        this.appAnalyticsKeyDtoUtil = appAnalyticsKeyDtoUtil;
    }

    @Override
    public AppAnalyticsEntity reqToEntity(AppAnalyticsReq appAnalyticsReq) {
        return new AppAnalyticsEntity()
                .setDescription(appAnalyticsReq.getDescription());
    }

    @Override
    public AppAnalyticsRes entityToRes(AppAnalyticsEntity appAnalyticsEntity) {
        return new AppAnalyticsRes()
                .setId(appAnalyticsEntity.getId())
                .setDescription(appAnalyticsEntity.getDescription());
    }

    @Override
    public AppAnalyticsRes prepRes(AppAnalyticsEntity appAnalyticsEntity) {
        AppAnalyticsRes appAnalyticsResDto = entityToRes(appAnalyticsEntity);

        if (appAnalyticsEntity.getAppAnalyticsKey().getAppAnalyticsKeyDataType().equals(AppAnalyticsKeyDataType.DATETIME)) {
            appAnalyticsResDto.setValue(String.valueOf(appAnalyticsEntity.getKeyValueDateTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
        } else if (appAnalyticsEntity.getAppAnalyticsKey().getAppAnalyticsKeyDataType().equals(AppAnalyticsKeyDataType.NUMBER)) {
            appAnalyticsResDto.setValue(appAnalyticsEntity.getKeyValueInt().toString());
        } else {
            appAnalyticsResDto.setValue(appAnalyticsEntity.getKeyValueText());
        }

        appAnalyticsResDto.setAppAnalyticsKey(appAnalyticsKeyDtoUtil.entityToRes(appAnalyticsEntity.getAppAnalyticsKey()));

        if (appAnalyticsEntity.getUser() != null) {
            appAnalyticsResDto.setUser(userDtoUtil.entityToRes(appAnalyticsEntity.getUser()));
        }

        return appAnalyticsResDto;
    }

    @Override
    public void setUpdatedValue(AppAnalyticsReq appAnalyticsReq, AppAnalyticsEntity appAnalyticsEntity) {

    }
}
