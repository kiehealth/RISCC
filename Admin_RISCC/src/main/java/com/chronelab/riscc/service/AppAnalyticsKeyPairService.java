package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.request.AppAnalyticsKeyPairReq;
import com.chronelab.riscc.dto.response.AppAnalyticsKeyPairRes;
import com.chronelab.riscc.dto.util.AppAnalyticsKeyPairDtoUtil;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyPairEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AppAnalyticsKeyPairRepo;
import com.chronelab.riscc.repo.AppAnalyticsKeyRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppAnalyticsKeyPairService {

    private static final Logger LOG = LogManager.getLogger();

    private final AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo;
    private final AppAnalyticsKeyRepo appAnalyticsKeyRepo;
    private final DtoUtil<AppAnalyticsKeyPairEntity, AppAnalyticsKeyPairReq, AppAnalyticsKeyPairRes> dtoUtil;

    @Autowired
    public AppAnalyticsKeyPairService(AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo, AppAnalyticsKeyRepo appAnalyticsKeyRepo,
                                      AppAnalyticsKeyPairDtoUtil appAnalyticsKeyPairDtoUtil) {
        this.appAnalyticsKeyPairRepo = appAnalyticsKeyPairRepo;
        this.appAnalyticsKeyRepo = appAnalyticsKeyRepo;
        this.dtoUtil = appAnalyticsKeyPairDtoUtil;
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public AppAnalyticsKeyPairRes save(AppAnalyticsKeyPairReq appAnalyticsKeyPairReqDto) {
        LOG.info("----- Saving App Analytics Type Pair. -----");

        Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyOneEntity = appAnalyticsKeyRepo.findById(appAnalyticsKeyPairReqDto.getAppAnalyticsKeyOneId());
        optionalAppAnalyticsKeyOneEntity.orElseThrow(() -> new CustomException("APA001"));

        Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity1 = appAnalyticsKeyPairRepo
                .findByKeyOneOrKeyTwo(optionalAppAnalyticsKeyOneEntity.get(), optionalAppAnalyticsKeyOneEntity.get());

        if (optionalAppAnalyticsKeyPairEntity1.isPresent()) {
            throw new CustomException("APA002");
        }

        AppAnalyticsKeyPairEntity appAnalyticsTypePair = new AppAnalyticsKeyPairEntity()
                .setKeyOne(optionalAppAnalyticsKeyOneEntity.get());

        if (appAnalyticsKeyPairReqDto.getAppAnalyticsKeyTwoId() != null) {
            Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyTwoEntity = appAnalyticsKeyRepo.findById(appAnalyticsKeyPairReqDto.getAppAnalyticsKeyTwoId());
            optionalAppAnalyticsKeyTwoEntity.orElseThrow(() -> new CustomException("APA001"));

            Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity2 = appAnalyticsKeyPairRepo
                    .findByKeyOneOrKeyTwo(optionalAppAnalyticsKeyTwoEntity.get(), optionalAppAnalyticsKeyTwoEntity.get());

            if (optionalAppAnalyticsKeyPairEntity2.isPresent()) {
                throw new CustomException("APA002");
            }

            appAnalyticsTypePair.setKeyTwo(optionalAppAnalyticsKeyTwoEntity.get());
        }

        return dtoUtil.prepRes(appAnalyticsKeyPairRepo.save(appAnalyticsTypePair));
    }

    public List<AppAnalyticsKeyPairRes> getByKeyTwoNotNull() {
        List<AppAnalyticsKeyPairEntity> appAnalyticsTypePairEntities = appAnalyticsKeyPairRepo.findAllByKeyTwoNotNull(Sort.by(Sort.Direction.ASC, "keyOne.keyTitle"));
        return appAnalyticsTypePairEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    public List<AppAnalyticsKeyPairRes> getAll() {
        List<AppAnalyticsKeyPairEntity> appAnalyticsTypePairEntities = appAnalyticsKeyPairRepo.findAll(Sort.by(Sort.Direction.ASC, "keyOne.keyTitle"));
        return appAnalyticsTypePairEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public AppAnalyticsKeyPairRes update(AppAnalyticsKeyPairReq appAnalyticsKeyPairReqDto) {
        LOG.info("----- Updating App Analytics Type Pair. -----");

        Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity = appAnalyticsKeyPairRepo.findById(appAnalyticsKeyPairReqDto.getId());
        optionalAppAnalyticsKeyPairEntity.orElseThrow(() -> new CustomException("APA003"));

        //App Analytics Type Two
        if (appAnalyticsKeyPairReqDto.getAppAnalyticsKeyTwoId() != null
                && (optionalAppAnalyticsKeyPairEntity.get().getKeyTwo() == null
                || !appAnalyticsKeyPairReqDto.getAppAnalyticsKeyTwoId().equals(optionalAppAnalyticsKeyPairEntity.get().getKeyTwo().getId()))
        ) {
            Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyEntity = appAnalyticsKeyRepo.findById(appAnalyticsKeyPairReqDto.getAppAnalyticsKeyTwoId());
            optionalAppAnalyticsKeyEntity.orElseThrow(() -> new CustomException("APA001"));

            Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity2 = appAnalyticsKeyPairRepo.findByKeyOneOrKeyTwo(
                    optionalAppAnalyticsKeyEntity.get(), optionalAppAnalyticsKeyEntity.get()
            );

            if (optionalAppAnalyticsKeyPairEntity2.isPresent()) {
                throw new CustomException("APA002");
            }

            optionalAppAnalyticsKeyPairEntity.get().setKeyTwo(optionalAppAnalyticsKeyEntity.get());
        } else {
            optionalAppAnalyticsKeyPairEntity.get().setKeyTwo(null);
        }

        return dtoUtil.prepRes(optionalAppAnalyticsKeyPairEntity.get());
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public void delete(Long appAnalyticsTypePairId) {
        LOG.info("----- Deleting App Analytics Key Pair. -----");

        Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity = appAnalyticsKeyPairRepo.findById(appAnalyticsTypePairId);
        optionalAppAnalyticsKeyPairEntity.orElseThrow(() -> new CustomException("APA003"));

        appAnalyticsKeyPairRepo.delete(optionalAppAnalyticsKeyPairEntity.get());
    }
}
