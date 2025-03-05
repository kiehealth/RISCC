package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.QuestionTypeReq;
import com.chronelab.riscc.dto.response.QuestionTypeRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.QuestionTypeEntity;
import com.chronelab.riscc.repo.QuestionRepo;
import com.chronelab.riscc.repo.QuestionTypeRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('QUESTION_TYPE')")
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionTypeService {

    private final static Logger logger = LogManager.getLogger();

    private final QuestionTypeRepo questionTypeRepo;
    private final QuestionRepo questionRepo;
    private final DtoUtil<QuestionTypeEntity, QuestionTypeReq, QuestionTypeRes> dtoUtil;
    private final PaginationDtoUtil<QuestionTypeEntity, QuestionTypeReq, QuestionTypeRes> paginationDtoUtil;

    private List<String> fields = Arrays.asList("title");
    private String sortBy = "title";// Default sortBy
    private Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder

    private Page<QuestionTypeEntity> questionTypeEntityPage;
    private List<QuestionTypeEntity> questionTypeEntities;

    @Autowired
    public QuestionTypeService(QuestionTypeRepo questionTypeRepo, QuestionRepo questionRepo, DtoUtil<QuestionTypeEntity, QuestionTypeReq,
            QuestionTypeRes> dtoUtil, PaginationDtoUtil<QuestionTypeEntity, QuestionTypeReq,
            QuestionTypeRes> paginationDtoUtil) {
        this.questionTypeRepo = questionTypeRepo;
        this.questionRepo = questionRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    private void modifySort(PaginationReqDto paginationReqDto) {
        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            sortBy = paginationReqDto.getSortBy();
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }
    }

    private PaginationResDto<QuestionTypeRes> buildRes() {
        List<QuestionTypeRes> questionTypeResDtos = questionTypeEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return paginationDtoUtil.prepPaginationDto(questionTypeEntityPage, questionTypeResDtos);
    }

    @PreAuthorize("hasAuthority('QUESTION_TYPE_C')")
    public QuestionTypeRes save(QuestionTypeReq questionTypeReqDto) {
        logger.info("----- Saving Question Type. -----");

        Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findByTitle(questionTypeReqDto.getTitle());
        if (optionalQuestionTypeEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Question Type already exists.");
        }

        return dtoUtil.prepRes(questionTypeRepo.save(dtoUtil.reqToEntity(questionTypeReqDto)));
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionTypeRes> get(PaginationReqDto paginationReqDto) {
        logger.info("----- Getting Question Type listing. -----");
        questionTypeEntityPage = null;
        questionTypeEntities = null;

        modifySort(paginationReqDto);

        if (paginationReqDto.getPageSize() > 0) {
            questionTypeEntityPage = questionTypeRepo.findAll(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            questionTypeEntities = questionTypeEntityPage.getContent();
        } else {
            questionTypeEntities = questionTypeRepo.findAll(Sort.by(sortOrder, sortBy));
        }

        return buildRes();
    }

    @PreAuthorize("hasAuthority('QUESTION_TYPE_U')")
    public QuestionTypeRes update(QuestionTypeReq questionTypeReqDto) {
        logger.info("----- Updating Question Type. -----");

        Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findById(questionTypeReqDto.getId());
        optionalQuestionTypeEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Type does not exist."));

        if (questionTypeReqDto.getTitle() != null && !questionTypeReqDto.getTitle().equals(optionalQuestionTypeEntity.get().getTitle())
                && questionTypeRepo.findByTitle(questionTypeReqDto.getTitle()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Question Type already exists.");
        }
        dtoUtil.setUpdatedValue(questionTypeReqDto, optionalQuestionTypeEntity.get());
        return dtoUtil.prepRes(optionalQuestionTypeEntity.get());
    }

    @PreAuthorize("hasAuthority('QUESTION_TYPE_D')")
    public void delete(Long questionTypeId) {
        logger.info("----- Deleting Question Type. -----");

        Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findById(questionTypeId);
        optionalQuestionTypeEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Type does not exist."));

        questionTypeRepo.delete(optionalQuestionTypeEntity.get());
    }
}
