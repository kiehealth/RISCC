package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.AppVersionDto;
import com.chronelab.riscc.dto.request.NotificationReq;
import com.chronelab.riscc.entity.AppVersionEntity;
import com.chronelab.riscc.enums.NotificationType;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AppVersionRepo;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppVersionService {

    private static Logger LOG = LogManager.getLogger();

    private final AppVersionRepo appVersionRepo;

    private final NotificationService notificationService;

    @Autowired
    public AppVersionService(AppVersionRepo appVersionRepo, NotificationService notificationService) {
        this.appVersionRepo = appVersionRepo;
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasAuthority('APP_VERSION_U')")
    public AppVersionDto update(AppVersionDto appVersionDto) throws FirebaseMessagingException, IOException {
        LOG.info("----- Updating App Version. -----");
        Optional<AppVersionEntity> optionalAppVersionEntity = appVersionRepo.findById(appVersionDto.getId());
        optionalAppVersionEntity.orElseThrow(() -> new CustomException("APV001"));

        if (appVersionDto.getFamily() != null && !appVersionDto.getFamily().equalsIgnoreCase(optionalAppVersionEntity.get().getFamily())) {
            optionalAppVersionEntity.get().setFamily(appVersionDto.getFamily());
        }
        if (appVersionDto.getVersion() != null && !appVersionDto.getVersion().equalsIgnoreCase(optionalAppVersionEntity.get().getVersion())) {
            optionalAppVersionEntity.get().setVersion(appVersionDto.getVersion());
        }
        if (appVersionDto.isForceUpdate() != optionalAppVersionEntity.get().isForceUpdate()) {
            optionalAppVersionEntity.get().setForceUpdate(appVersionDto.isForceUpdate());
        }
        if (appVersionDto.getUrl() != null && !appVersionDto.getUrl().equalsIgnoreCase(optionalAppVersionEntity.get().getUrl())) {
            optionalAppVersionEntity.get().setUrl(appVersionDto.getUrl());
        }

        //Broadcast push notification
        NotificationReq notificationReqDto = new NotificationReq();
        notificationReqDto.setTitle("New App Version " + appVersionDto.getVersion());
        notificationReqDto.setDescription("New App Version is available, please update your App. From: " + optionalAppVersionEntity.get().getUrl());
        notificationReqDto.setNotificationType(NotificationType.BROADCAST);

        notificationService.save(notificationReqDto);

        return appVersionDto
                .setFamily(optionalAppVersionEntity.get().getFamily())
                .setVersion(optionalAppVersionEntity.get().getVersion())
                .setForceUpdate(optionalAppVersionEntity.get().isForceUpdate())
                .setUrl(optionalAppVersionEntity.get().getUrl());
    }

    public List<AppVersionDto> get() {
        List<AppVersionDto> appVersionDtos = new ArrayList<>();
        List<AppVersionEntity> appVersionEntities = appVersionRepo.findAll();
        appVersionEntities.forEach(appVersionEntity -> appVersionDtos.add(
                new AppVersionDto()
                        .setId(appVersionEntity.getId())
                        .setFamily(appVersionEntity.getFamily())
                        .setVersion(appVersionEntity.getVersion())
                        .setForceUpdate(appVersionEntity.isForceUpdate())
                        .setUrl(appVersionEntity.getUrl())
                )
        );
        return appVersionDtos;
    }
}
