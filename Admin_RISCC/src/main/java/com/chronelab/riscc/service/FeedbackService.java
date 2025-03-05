package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.FeedbackReq;
import com.chronelab.riscc.dto.response.FeedbackRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.FeedbackDtoUtil;
import com.chronelab.riscc.entity.FeedbackEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.FeedbackRepo;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.specification.FeedbackSpecificationBuilder;
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
public class FeedbackService {

    private static final Logger LOG = LogManager.getLogger();
    private final FeedbackRepo feedbackRepo;
    private final UserRepo userRepo;
    private final DtoUtil<FeedbackEntity, FeedbackReq, FeedbackRes> dtoUtil;
    private final PaginationDtoUtil<FeedbackEntity, FeedbackReq, FeedbackRes> paginationDtoUtil;

    @Autowired
    public FeedbackService(FeedbackRepo feedbackRepo, UserRepo userRepo, FeedbackDtoUtil feedbackDtoUtil, PaginationDtoUtil<FeedbackEntity, FeedbackReq, FeedbackRes> paginationDtoUtil) {
        this.feedbackRepo = feedbackRepo;
        this.userRepo = userRepo;
        this.dtoUtil = feedbackDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('FEEDBACK_C')")
    public FeedbackRes save(FeedbackReq feedbackReqDto) {
        LOG.info("----- Saving Feedback. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        FeedbackEntity feedback = dtoUtil.reqToEntity(feedbackReqDto).setUser(optionalUserEntity.get());
        return dtoUtil.prepRes(feedbackRepo.save(feedback));
    }

    @PreAuthorize("hasAuthority('FEEDBACK_RA')")
    public PaginationResDto<FeedbackRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Feedbacks. -----");

        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null) {
            sortBy = paginationReqDto.getSortBy();
        }
        if (paginationReqDto.getSortOrder() != null && paginationReqDto.getSortOrder().equalsIgnoreCase("ASC")) {
            sortOrder = Sort.Direction.ASC;
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (paginationReqDto.getSearchTerm() != null) {
            searchCriteriaList.add(new SearchCriteria("title", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("description", ":", paginationReqDto.getSearchTerm(), true));
        }

        Page<FeedbackEntity> feedbackEntityPage = null;
        List<FeedbackEntity> feedbackEntities;
        if (paginationReqDto.getPageSize() > 0) {
            feedbackEntityPage = feedbackRepo.findAll(new FeedbackSpecificationBuilder().with(searchCriteriaList).build(),
                    PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            feedbackEntities = feedbackEntityPage.getContent();
        } else {
            feedbackEntities = feedbackRepo.findAll(new FeedbackSpecificationBuilder().with(searchCriteriaList).build(), Sort.by(sortOrder, sortBy));
        }

        List<FeedbackRes> feedbackResDtos = feedbackEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(feedbackEntityPage, feedbackResDtos);

        /*
        List<String> fields = Arrays.asList("title", "description", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
        return new PaginationDtoUtil().paginate(paginationReqDto, fields, sortBy, sortOrder, feedbackRepo, dtoUtil);*/
    }

    @PreAuthorize("hasAuthority('FEEDBACK')")
    public PaginationResDto<FeedbackRes> getByUser(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Feedbacks by User. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        List<String> fields = Arrays.asList("title", "description", "createdDate");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<FeedbackEntity> feedbackEntityPage = null;
        List<FeedbackEntity> feedbackEntities;
        if (paginationReqDto.getPageSize() > 0) {
            feedbackEntityPage = feedbackRepo.findAllByUser(optionalUserEntity.get(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            feedbackEntities = feedbackEntityPage.getContent();
        } else {
            feedbackEntities = feedbackRepo.findAllByUser(optionalUserEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<FeedbackRes> feedbackResDtos = feedbackEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(feedbackEntityPage, feedbackResDtos);
    }
}
