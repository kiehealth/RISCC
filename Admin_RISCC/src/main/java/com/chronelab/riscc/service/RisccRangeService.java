package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.request.RisccRangeReq;
import com.chronelab.riscc.dto.response.RisccRangeRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import com.chronelab.riscc.entity.RisccRangeAndMessageEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.QuestionnaireRepo;
import com.chronelab.riscc.repo.RisccRangeRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('SETTING')")
@Service
@Transactional(rollbackFor = Exception.class)
public class RisccRangeService {

    private static final Logger LOG = LogManager.getLogger();

    private final DtoUtil<RisccRangeAndMessageEntity, RisccRangeReq, RisccRangeRes> dtoUtil;
    private final RisccRangeRepo risccRangeRepo;
    private final QuestionnaireRepo questionnaireRepo;
    private final PaginationDtoUtil<RisccRangeAndMessageEntity, RisccRangeReq, RisccRangeRes> paginationDtoUtil;

    @Autowired
    public RisccRangeService(DtoUtil<RisccRangeAndMessageEntity, RisccRangeReq, RisccRangeRes> dtoUtil, RisccRangeRepo risccRangeRepo, QuestionnaireRepo questionnaireRepo, PaginationDtoUtil<RisccRangeAndMessageEntity, RisccRangeReq, RisccRangeRes> paginationDtoUtil) {
        this.dtoUtil = dtoUtil;
        this.risccRangeRepo = risccRangeRepo;
        this.questionnaireRepo = questionnaireRepo;
        this.paginationDtoUtil = paginationDtoUtil;
    }


    public RisccRangeRes save(RisccRangeReq risccRange) {
        LOG.info("--------- Saving Riscc Range. ------------");
        return dtoUtil.prepRes(risccRangeRepo.save(prepareRisccRange(risccRange)));

    }

    private RisccRangeAndMessageEntity prepareRisccRange(RisccRangeReq risccRange) {

        RisccRangeAndMessageEntity risccRangeAndMessageEntity = dtoUtil.reqToEntity(risccRange);
        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(risccRange.getQuestionaryId());
        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
        risccRangeAndMessageEntity.setQuestionnaire(optionalQuestionnaireEntity.get());
        return risccRangeAndMessageEntity;
    }


    public List<RisccRangeRes> get() {
        LOG.info("------------ Getting Riscc Range. ---------");
        List<RisccRangeAndMessageEntity> risccRangeAndMessageEntities = risccRangeRepo.findAll();
        return risccRangeAndMessageEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('SETTING_U')")
    public Object update(RisccRangeReq risccRange) {
        LOG.info("-------- Updating Riscc Value. --------");

        Optional<RisccRangeAndMessageEntity> optionalRisccRangeAndMessageEntity = risccRangeRepo.findById(risccRange.getId());
        optionalRisccRangeAndMessageEntity.orElseThrow(() -> new CustomException("RIS001"));

        RisccRangeAndMessageEntity risccRangeAndMessageEntity = optionalRisccRangeAndMessageEntity.get();

        if (risccRange.getFromValue() != null && !risccRange.getFromValue().equals(risccRangeAndMessageEntity.getFrom_value())) {
            risccRangeAndMessageEntity.setFrom_value(risccRange.getFromValue());
        }
        if (risccRange.getToValue() != null && !risccRange.getToValue().equals(risccRangeAndMessageEntity.getTo_value())) {
            risccRangeAndMessageEntity.setTo_value(risccRange.getToValue());
        }
        if (risccRange.getMessage() != null && !risccRange.getMessage().equals(risccRangeAndMessageEntity.getMessage())) {
            risccRangeAndMessageEntity.setMessage(risccRange.getMessage());
        }
        if (risccRange.getMoreInfo() != null && !risccRange.getMoreInfo().equals(risccRangeAndMessageEntity.getMoreInfo())){
            risccRangeAndMessageEntity.setMoreInfo(risccRange.getMoreInfo());
        }

        if (risccRange.getQuestionaryId() != null && !risccRange.getQuestionaryId().equals(risccRangeAndMessageEntity.getQuestionnaire().getId())) {
            Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(risccRange.getQuestionaryId());
            optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
            risccRangeAndMessageEntity.setQuestionnaire(optionalQuestionnaireEntity.get());
        }
        dtoUtil.setUpdatedValue(risccRange, risccRangeAndMessageEntity);
        return dtoUtil.prepRes(risccRangeAndMessageEntity);
    }

    public void delete(Long risccRangeId) {
        LOG.info("---------- Deleting Riscc Range. --------");
        Optional<RisccRangeAndMessageEntity> optionalRisccRangeAndMessageEntity = risccRangeRepo.findById(risccRangeId);
        optionalRisccRangeAndMessageEntity.orElseThrow(() -> new CustomException("RIS001"));

        risccRangeRepo.delete(optionalRisccRangeAndMessageEntity.get());
    }
}

