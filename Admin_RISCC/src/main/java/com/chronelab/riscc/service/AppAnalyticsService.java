package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.AppAnalyticsDto;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.AppAnalyticsReq;
import com.chronelab.riscc.dto.response.AppAnalyticsRes;
import com.chronelab.riscc.dto.util.AppAnalyticsDtoUtil;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.general.UserDtoUtil;
import com.chronelab.riscc.entity.AppAnalyticsEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyEntity;
import com.chronelab.riscc.entity.AppAnalyticsKeyPairEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.AppAnalyticsKeyPairRepo;
import com.chronelab.riscc.repo.AppAnalyticsKeyRepo;
import com.chronelab.riscc.repo.AppAnalyticsRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.specification.AppAnalyticsSpecificationBuilder;
import com.chronelab.riscc.repo.specification.SearchCriteria;
import com.chronelab.riscc.util.CsvUtil;
import com.chronelab.riscc.util.GeneralUtil;
import com.chronelab.riscc.util.PdfUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('APP_ANALYTICS')")
@Service
@Transactional(rollbackFor = Exception.class)
public class AppAnalyticsService {

    private final static Logger LOG = LogManager.getLogger();

    private final AppAnalyticsRepo appAnalyticsRepo;
    private final AppAnalyticsKeyRepo appAnalyticsKeyRepo;
    private final UserRepo userRepo;
    private final DtoUtil<AppAnalyticsEntity, AppAnalyticsReq, AppAnalyticsRes> dtoUtil;
    private final AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo;
    private final PdfUtil pdfUtil;
    private final CsvUtil csvUtil;
    private final PaginationDtoUtil<AppAnalyticsEntity, AppAnalyticsReq, AppAnalyticsRes> paginationDtoUtil;
    private final UserDtoUtil userDtoUtil;

    @Autowired
    public AppAnalyticsService(AppAnalyticsRepo appAnalyticsRepo, UserRepo userRepo,
                               AppAnalyticsKeyRepo appAnalyticsKeyRepo,
                               AppAnalyticsDtoUtil appAnalyticsDtoUtil, AppAnalyticsKeyPairRepo appAnalyticsKeyPairRepo,
                               PdfUtil pdfUtil, CsvUtil csvUtil, PaginationDtoUtil<AppAnalyticsEntity, AppAnalyticsReq, AppAnalyticsRes> paginationDtoUtil, UserDtoUtil userDtoUtil) {
        this.appAnalyticsRepo = appAnalyticsRepo;
        this.appAnalyticsKeyRepo = appAnalyticsKeyRepo;
        this.userRepo = userRepo;
        this.dtoUtil = appAnalyticsDtoUtil;
        this.appAnalyticsKeyPairRepo = appAnalyticsKeyPairRepo;
        this.pdfUtil = pdfUtil;
        this.csvUtil = csvUtil;
        this.paginationDtoUtil = paginationDtoUtil;
        this.userDtoUtil = userDtoUtil;
    }

    public PaginationResDto<AppAnalyticsRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting App Analytics. -----");

        List<String> fields = Arrays.asList("description", "keyValueDateTime", "keyValueInt", "keyValueText", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortBy().equalsIgnoreCase("value")) {
            sortBy = "keyValueDateTime";
        }
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, appAnalyticsRepo, dtoUtil);
    }

    public PaginationResDto<AppAnalyticsRes> getByUserAndType(Long userId, Long appAnalyticsTypeId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting App Analytics by User. -----");

        List<String> fields = Arrays.asList("description", "keyValueDateTime", "keyValueInt", "keyValueText", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<AppAnalyticsEntity> appAnalyticsEntityPage = null;
        List<AppAnalyticsEntity> appAnalyticsEntities;
        if (paginationReqDto.getPageSize() > 0) {
            if (userId != null && appAnalyticsTypeId != null) {
                appAnalyticsEntityPage = appAnalyticsRepo.findAllByAppAnalyticsKey_IdAndUser_Id(appAnalyticsTypeId, userId, PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            } else if (userId != null) {
                appAnalyticsEntityPage = appAnalyticsRepo.findAllByUser_Id(userId, PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            } else if (appAnalyticsTypeId != null) {
                appAnalyticsEntityPage = appAnalyticsRepo.findAllByAppAnalyticsKey_Id(appAnalyticsTypeId, PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            } else {
                appAnalyticsEntityPage = appAnalyticsRepo.findAll(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            }
            appAnalyticsEntities = appAnalyticsEntityPage.getContent();
        } else {
            if (userId != null && appAnalyticsTypeId != null) {
                appAnalyticsEntities = appAnalyticsRepo.findAllByAppAnalyticsKey_IdAndUser_Id(appAnalyticsTypeId, userId, Sort.by(sortOrder, sortBy));
            } else if (userId != null) {
                appAnalyticsEntities = appAnalyticsRepo.findAllByUser_Id(userId, Sort.by(sortOrder, sortBy));
            } else if (appAnalyticsTypeId != null) {
                appAnalyticsEntities = appAnalyticsRepo.findAllByAppAnalyticsKey_Id(appAnalyticsTypeId, Sort.by(sortOrder, sortBy));
            } else {
                appAnalyticsEntities = appAnalyticsRepo.findAll(Sort.by(sortOrder, sortBy));
            }
        }

        List<AppAnalyticsRes> appAnalyticsResDtos = appAnalyticsEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(appAnalyticsEntityPage, appAnalyticsResDtos);
    }

    public PaginationResDto<AppAnalyticsDto> getWithFilter(PaginationReqDto paginationReqDto, List<Long> userIds, List<Long> appAnalyticsTypePairIds) {
        LOG.info("----- Getting App Analytics. -----");

        PaginationResDto<AppAnalyticsDto> paginationResDto = new PaginationResDto<>();
        List<AppAnalyticsDto> appAnalyticsDtos = new ArrayList<>();

        //User
        List<UserEntity> userEntities = new ArrayList<>();
        if (userIds != null && userIds.size() > 0) {
            for (Long userId : userIds) {
                Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                userEntities.add(optionalUserEntity.get());
            }
        } else {
            userEntities.addAll(userRepo.findAll());
        }

        List<UserEntity> sortedUsers = userEntities.stream()
                .sorted(Comparator.comparing(UserEntity::getFirstName))
                .collect(Collectors.toList());

        //App Analytics Type
        Map<AppAnalyticsKeyEntity, AppAnalyticsKeyEntity> appAnalyticsTypeEntities = new HashMap<>();
        if (appAnalyticsTypePairIds != null && appAnalyticsTypePairIds.size() > 0) {
            for (Long appAnalyticsTypePairId : appAnalyticsTypePairIds) {

                Optional<AppAnalyticsKeyPairEntity> optionalAppAnalyticsKeyPairEntity = appAnalyticsKeyPairRepo.findById(appAnalyticsTypePairId);
                optionalAppAnalyticsKeyPairEntity.orElseThrow(() -> new CustomException("APA003"));

                if (optionalAppAnalyticsKeyPairEntity.get().getKeyTwo() == null) {
                    throw new CustomException("APA001");
                }

                appAnalyticsTypeEntities.put(optionalAppAnalyticsKeyPairEntity.get().getKeyOne(), optionalAppAnalyticsKeyPairEntity.get().getKeyTwo());
            }
        } else {
            List<AppAnalyticsKeyPairEntity> appAnalyticsTypePairEntities = appAnalyticsKeyPairRepo.findAll();
            for (AppAnalyticsKeyPairEntity appAnalyticsKeyPairEntity : appAnalyticsTypePairEntities) {
                appAnalyticsTypeEntities.put(appAnalyticsKeyPairEntity.getKeyOne(), appAnalyticsKeyPairEntity.getKeyTwo());
            }
        }

        for (UserEntity userEntity : sortedUsers) {
            for (Map.Entry<AppAnalyticsKeyEntity, AppAnalyticsKeyEntity> appAnalyticsTypeEntityPair : appAnalyticsTypeEntities.entrySet()) {
                appAnalyticsDtos.addAll(this.filterByUser(userEntity, appAnalyticsTypeEntityPair.getKey(), appAnalyticsTypeEntityPair.getValue()));
            }
        }
        paginationResDto.setData(appAnalyticsDtos);
        return paginationResDto;
    }

    private List<AppAnalyticsDto> filterByUser(UserEntity userEntity, AppAnalyticsKeyEntity type1, AppAnalyticsKeyEntity type2) {

        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        searchCriteriaList.add(new SearchCriteria("userIds", ":", userEntity, false));
        searchCriteriaList.add(new SearchCriteria("appAnalyticsKey", ":", Arrays.asList(type1, type2), false));

        List<AppAnalyticsEntity> appAnalyticsEntities = appAnalyticsRepo.findAll(new AppAnalyticsSpecificationBuilder()
                .with(searchCriteriaList).build(), Sort.by(sortOrder, sortBy));

        List<AppAnalyticsDto> appAnalyticsDtos = new ArrayList<>();
        List<AppAnalyticsEntity> appAnalyticsEntitiesToDelete = new ArrayList<>();//If In and Out time difference is 0 sec
        for (int i = 0; i < appAnalyticsEntities.size() - 1; ) {
            AppAnalyticsEntity appAnalyticsEntity1 = appAnalyticsEntities.get(i);//Should include _OUT, _STOP
            AppAnalyticsEntity appAnalyticsEntity2 = appAnalyticsEntities.get(i + 1);//Should include _IN, _PLAY

            if (appAnalyticsEntity1.getAppAnalyticsKey().getKeyTitle().endsWith("_IN")
                    || appAnalyticsEntity1.getAppAnalyticsKey().getKeyTitle().endsWith("_PLAY")
                    || appAnalyticsEntity2.getAppAnalyticsKey().getKeyTitle().endsWith("_OUT")
                    || appAnalyticsEntity2.getAppAnalyticsKey().getKeyTitle().endsWith("_STOP")) {
                i++;
                continue;
            }

            long epochMilis1 = appAnalyticsEntity1.getKeyValueDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long epochMilis2 = appAnalyticsEntity2.getKeyValueDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
            long differenceInSec = (epochMilis1 - epochMilis2) / 1000;
            if (differenceInSec <= 0) {
                appAnalyticsEntitiesToDelete.add(appAnalyticsEntity1);
                appAnalyticsEntitiesToDelete.add(appAnalyticsEntity2);
                i += 2;
                continue;
            }

            AppAnalyticsDto appAnalyticsDto = new AppAnalyticsDto();
            appAnalyticsDto.setTitle1(GeneralUtil.formatString(appAnalyticsEntity2.getAppAnalyticsKey().getKeyTitle()));//Should include _IN, _PLAY
            appAnalyticsDto.setValue1(String.valueOf(appAnalyticsEntity2.getKeyValueDateTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
            appAnalyticsDto.setTitle2(GeneralUtil.formatString(appAnalyticsEntity1.getAppAnalyticsKey().getKeyTitle()));//Should include _OUT, _STOP
            appAnalyticsDto.setValue2(String.valueOf(appAnalyticsEntity1.getKeyValueDateTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
            appAnalyticsDto.setDifference(GeneralUtil.convertSecToHourMinuteSecond(differenceInSec));

            appAnalyticsDto.setUser(userDtoUtil.entityToRes(appAnalyticsEntity1.getUser()));

            appAnalyticsDtos.add(appAnalyticsDto);

            i += 2;
        }

        if (appAnalyticsEntitiesToDelete.size() > 0) {
            appAnalyticsRepo.deleteAll(appAnalyticsEntitiesToDelete);
        }

        return appAnalyticsDtos;
    }

    public String exportFile(String fileType, int timeZoneOffsetInMinute, List<Long> userIds, List<Long> appAnalyticsTypePairIds) throws IOException {
        LOG.info("----- Exporting App Analytics data to PDF. -----");

        List<AppAnalyticsDto> appAnalyticsDtos = this.getWithFilter(null, userIds, appAnalyticsTypePairIds).getData();

        List<String> heading = Arrays.asList("SN", "Title1", "Value1", "Title2", "Value2", "Difference (hh:mm:ss)", "User");

        List<List<String>> data = new ArrayList<>();
        int i = 1;
        for (AppAnalyticsDto appAnalyticsDto : appAnalyticsDtos) {
            List<String> row = new ArrayList<>();

            LocalDateTime localDateTime1 = LocalDateTime.ofEpochSecond((Long.parseLong(appAnalyticsDto.getValue1())) / 1000, 0, ZoneOffset.ofTotalSeconds(timeZoneOffsetInMinute * 60));
            LocalDateTime localDateTime2 = LocalDateTime.ofEpochSecond((Long.parseLong(appAnalyticsDto.getValue2())) / 1000, 0, ZoneOffset.ofTotalSeconds(timeZoneOffsetInMinute * 60));

            row.add(String.valueOf(i++));
            row.add(appAnalyticsDto.getTitle1());
            row.add(GeneralUtil.formatDateTime(localDateTime1));
            row.add(appAnalyticsDto.getTitle2());
            row.add(GeneralUtil.formatDateTime(localDateTime2));
            row.add(appAnalyticsDto.getDifference());
            row.add(appAnalyticsDto.getUser().getFirstName() + " " + appAnalyticsDto.getUser().getLastName());

            data.add(row);
        }

        if (fileType.equalsIgnoreCase("pdf")) {
            return pdfUtil.createPdf("app_analytics_report", null, heading, data);
        } else {
            return csvUtil.createCsv("app_analytics_report", heading, data);
        }
    }
}
