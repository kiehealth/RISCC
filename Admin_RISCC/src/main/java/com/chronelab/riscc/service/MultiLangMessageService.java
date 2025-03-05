package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.MultiLangMessageReq;
import com.chronelab.riscc.dto.response.MultiLangMessageRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.MultiLangMessageDtoUtil;
import com.chronelab.riscc.entity.MultiLangMessageEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.MultiLangMessageRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@PreAuthorize("hasAuthority('MULTI_LANG_MESSAGE')")
@Service
@Transactional(rollbackFor = Exception.class)
public class MultiLangMessageService {

    private static final Logger LOG = LogManager.getLogger();

    private final MultiLangMessageRepo multiLangMessageRepo;
    private final DtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> dtoUtil;
    private final PaginationDtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> paginationDtoUtil;

    @Autowired
    public MultiLangMessageService(MultiLangMessageRepo multiLangMessageRepo, MultiLangMessageDtoUtil multiLangMessageDtoUtil, PaginationDtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> paginationDtoUtil) {
        this.multiLangMessageRepo = multiLangMessageRepo;
        this.dtoUtil = multiLangMessageDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    public PaginationResDto<MultiLangMessageRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting MultiLangMessage. -----");

        List<String> fields = Arrays.asList("code", "english", "swedish", "spanish");
        String sortBy = "code";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, multiLangMessageRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('MULTI_LANG_MESSAGE_U')")
    public MultiLangMessageRes update(MultiLangMessageReq multiLangMessageReq) {
        LOG.info("----- Updating MultiLangMessage. -----");

        Optional<MultiLangMessageEntity> MultiLangMessageEntity = multiLangMessageRepo.findById(multiLangMessageReq.getId());
        MultiLangMessageEntity.orElseThrow(() -> new CustomException("MLM001"));

        dtoUtil.setUpdatedValue(multiLangMessageReq, MultiLangMessageEntity.get());

        return dtoUtil.prepRes(MultiLangMessageEntity.get());
    }
}
