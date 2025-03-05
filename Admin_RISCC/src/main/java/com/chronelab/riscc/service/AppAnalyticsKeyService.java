package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.AppAnalyticsKeyPairReq;
import com.chronelab.riscc.dto.request.AppAnalyticsKeyReq;
import com.chronelab.riscc.dto.response.AppAnalyticsKeyRes;
import com.chronelab.riscc.dto.util.AppAnalyticsKeyDtoUtil;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AppAnalyticsKeyPairRepo;
import com.chronelab.riscc.repo.AppAnalyticsKeyRepo;
import com.chronelab.riscc.repo.AppAnalyticsRepo;
import com.chronelab.riscc.repo.specification.AppAnalyticsKeySpecificationBuilder;
import com.chronelab.riscc.repo.specification.SearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppAnalyticsKeyService {

    private final static Logger LOG = LogManager.getLogger();

    private final AppAnalyticsKeyRepo appAnalyticsKeyRepo;
    private final AppAnalyticsRepo appAnalyticsRepo;
    private final AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo;
    private final DtoUtil<AppAnalyticsKeyEntity, AppAnalyticsKeyReq, AppAnalyticsKeyRes> dtoUtil;

    private final AppAnalyticsKeyPairService appAnalyticsKeyPairService;
    private final PaginationDtoUtil<AppAnalyticsKeyEntity, AppAnalyticsKeyReq, AppAnalyticsKeyRes> paginationDtoUtil;

    @Autowired
    public AppAnalyticsKeyService(AppAnalyticsKeyRepo appAnalyticsKeyRepo, AppAnalyticsRepo appAnalyticsRepo, AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo, AppAnalyticsKeyDtoUtil appAnalyticsKeyDtoUtil,
                                  AppAnalyticsKeyPairService appAnalyticsKeyPairService, PaginationDtoUtil<AppAnalyticsKeyEntity, AppAnalyticsKeyReq, AppAnalyticsKeyRes> paginationDtoUtil) {
        this.appAnalyticsKeyRepo = appAnalyticsKeyRepo;
        this.appAnalyticsRepo = appAnalyticsRepo;
        this.appAnalyticsKeyPairRepo = appAnalyticsKeyPairRepo;
        this.dtoUtil = appAnalyticsKeyDtoUtil;
        this.appAnalyticsKeyPairService = appAnalyticsKeyPairService;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public AppAnalyticsKeyRes save(AppAnalyticsKeyReq appAnalyticsKeyReqDto) {
        LOG.info("----- Saving App Analytics Type. -----");

        AppAnalyticsKeyEntity appAnalyticsType = dtoUtil.reqToEntity(appAnalyticsKeyReqDto);
        appAnalyticsKeyRepo.save(appAnalyticsType);

        if (appAnalyticsType.getKeyTitle().endsWith("IN") || appAnalyticsType.getKeyTitle().endsWith("PLAY")) {
            AppAnalyticsKeyPairReq appAnalyticsKeyPairReqDto = new AppAnalyticsKeyPairReq();
            appAnalyticsKeyPairReqDto.setAppAnalyticsKeyOneId(appAnalyticsType.getId());
            appAnalyticsKeyPairService.save(appAnalyticsKeyPairReqDto);
        }

        return dtoUtil.prepRes(appAnalyticsType);
    }

    public PaginationResDto<AppAnalyticsKeyRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting App Analytics Type. -----");

        List<String> fields = Arrays.asList("keyTitle", "description", "appAnalyticsKeyDataType");
        String sortBy = "keyTitle";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, appAnalyticsKeyRepo, dtoUtil);
    }

    public PaginationResDto<AppAnalyticsKeyRes> getByFilter(String title1, String title2, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting App Analytics Type. -----");

        List<String> fields = Arrays.asList("keyTitle", "description", "appAnalyticsKeyDataType");
        String sortBy = "keyTitle";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortBy().equalsIgnoreCase("title")) {
            sortBy = "keyTitle";
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (title1 != null) {
            searchCriteriaList.add(new SearchCriteria("title1", ":", title1, true));
        }
        if (title2 != null) {
            searchCriteriaList.add(new SearchCriteria("title2", ":", title2, true));
        }

        Page<AppAnalyticsKeyEntity> appAnalyticsTypeEntityPage = null;
        List<AppAnalyticsKeyEntity> appAnalyticsTypeEntities;
        if (paginationReqDto.getPageSize() > 0) {
            appAnalyticsTypeEntityPage = appAnalyticsKeyRepo.findAll(new AppAnalyticsKeySpecificationBuilder().with(searchCriteriaList).build(),
                    PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            appAnalyticsTypeEntities = appAnalyticsTypeEntityPage.getContent();
        } else {
            appAnalyticsTypeEntities = appAnalyticsKeyRepo.findAll(new AppAnalyticsKeySpecificationBuilder().with(searchCriteriaList).build(),
                    Sort.by(sortOrder, sortBy));
        }

        List<AppAnalyticsKeyRes> appAnalyticsKeyResDtos = appAnalyticsTypeEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(appAnalyticsTypeEntityPage, appAnalyticsKeyResDtos);
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public AppAnalyticsKeyRes update(AppAnalyticsKeyReq appAnalyticsKeyReqDto) {
        LOG.info("----- Updating App Analytics Type. -----");

        Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyEntity = appAnalyticsKeyRepo.findById(appAnalyticsKeyReqDto.getId());
        optionalAppAnalyticsKeyEntity.orElseThrow(() -> new CustomException("APA001"));

        dtoUtil.setUpdatedValue(appAnalyticsKeyReqDto, optionalAppAnalyticsKeyEntity.get());
        return dtoUtil.prepRes(optionalAppAnalyticsKeyEntity.get());
    }

    @PreAuthorize("hasAuthority('APP_ANALYTICS')")
    public void delete(Long appAnalyticsTypeId) {
        LOG.info("----- Deleting App Analytics Type. -----");

        Optional<AppAnalyticsKeyEntity> optionalAppAnalyticsKeyEntity = appAnalyticsKeyRepo.findById(appAnalyticsTypeId);
        optionalAppAnalyticsKeyEntity.orElseThrow(() -> new CustomException("APA001"));

        appAnalyticsRepo.deleteAllByAppAnalyticsKey(optionalAppAnalyticsKeyEntity.get());
        appAnalyticsKeyPairRepo.deleteAllByKeyOneOrKeyTwo(optionalAppAnalyticsKeyEntity.get(), optionalAppAnalyticsKeyEntity.get());

        appAnalyticsKeyRepo.delete(optionalAppAnalyticsKeyEntity.get());
    }
}
