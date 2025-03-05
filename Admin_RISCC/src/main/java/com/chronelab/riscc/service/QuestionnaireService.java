package com.chronelab.riscc.service;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.QuestionQuestionnaireReq;
import com.chronelab.riscc.dto.request.QuestionnaireReq;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.QuestionnaireDtoUtil;
import com.chronelab.riscc.entity.QuestionEntity;
import com.chronelab.riscc.entity.QuestionQuestionnaireEntity;
import com.chronelab.riscc.entity.QuestionnaireEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.GroupQuestionnaireRepo;
import com.chronelab.riscc.repo.QuestionQuestionnaireRepo;
import com.chronelab.riscc.repo.QuestionRepo;
import com.chronelab.riscc.repo.QuestionnaireRepo;
import com.chronelab.riscc.repo.projection.QuestionnaireProjection;
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

@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionnaireService {

    private static final Logger LOG = LogManager.getLogger();

    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionRepo questionRepo;
    private final QuestionQuestionnaireRepo questionQuestionnaireRepo;
    private final DtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> dtoUtil;
    private final PaginationDtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> paginationDtoUtil;
    private final GroupQuestionnaireRepo groupQuestionnaireRepo;

    @Autowired
    public QuestionnaireService(QuestionnaireRepo questionnaireRepo, QuestionRepo questionRepo, QuestionnaireDtoUtil questionnaireDtoUtil,
                                QuestionQuestionnaireRepo questionQuestionnaireRepo, PaginationDtoUtil<QuestionnaireEntity, QuestionnaireReq, QuestionnaireRes> paginationDtoUtil, GroupQuestionnaireRepo groupQuestionnaireRepo) {
        this.questionnaireRepo = questionnaireRepo;
        this.questionRepo = questionRepo;
        this.dtoUtil = questionnaireDtoUtil;
        this.questionQuestionnaireRepo = questionQuestionnaireRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
    }

    @PreAuthorize("hasAuthority('QUESTIONNAIRE_C')")
    public QuestionnaireRes save(QuestionnaireReq questionnaireReqDto) {
        LOG.info("----- Saving Questionnaire. -----");
        if (questionnaireRepo.findByTitle(questionnaireReqDto.getTitle()) != null) {
            throw new CustomException("QUE006");
        }

        QuestionnaireEntity questionnaire = dtoUtil.reqToEntity(questionnaireReqDto);

        if (questionnaireReqDto.getQuestionQuestionnaires() != null) {
            List<QuestionQuestionnaireEntity> questionQuestionnaires = new ArrayList<>();

            for (QuestionQuestionnaireReq questionQuestionnaireReq : questionnaireReqDto.getQuestionQuestionnaires()) {

                QuestionQuestionnaireEntity questionQuestionnaire = new QuestionQuestionnaireEntity();

                Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(questionQuestionnaireReq.getQuestionId());
                optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));
                questionQuestionnaire.setQuestion(optionalQuestionEntity.get())
                        .setDisplayOrder(questionQuestionnaireReq.getDisplayOrder())
                        .setQuestionnaire(questionnaire);
                questionQuestionnaires.add(questionQuestionnaire);
            }
            questionnaire.setQuestionQuestionnaires(questionQuestionnaires);
        }

        QuestionnaireEntity questionnaireEntity = questionnaireRepo.save(questionnaire);

        return dtoUtil.prepRes(questionnaireEntity);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionnaireRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Questionnaire. -----");

        List<String> fields = Arrays.asList("title");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, questionnaireRepo, dtoUtil);
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionnaireProjection> getLimited(PaginationReqDto paginationReqDto, List<String> fields) {
        LOG.info("----- Getting paginated Questionnaire with limited field data. -----");

        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null) {
            sortBy = paginationReqDto.getSortBy();
        }
        if (paginationReqDto.getSortOrder() != null && paginationReqDto.getSortOrder().equalsIgnoreCase("DESC")) {
            sortOrder = Sort.Direction.DESC;
        }

        Page<QuestionnaireProjection> pageQuestionnaireEntity = null;
        List<QuestionnaireProjection> questionnaireEntities;
        if (paginationReqDto.getPageSize() > 0) {
            pageQuestionnaireEntity = questionnaireRepo.findAllByCreatedDateNotNull(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            questionnaireEntities = pageQuestionnaireEntity.getContent();
        } else {
            questionnaireEntities = questionnaireRepo.findAllByCreatedDateNotNull(Sort.by(sortOrder, sortBy));
        }

        PaginationResDto<QuestionnaireProjection> paginationResDto = new PaginationResDto<>();

        if (pageQuestionnaireEntity != null) {
            paginationResDto
                    .setStartPosition(pageQuestionnaireEntity.getNumber() * pageQuestionnaireEntity.getSize() + 1)
                    .setEndPosition((pageQuestionnaireEntity.getNumber() * pageQuestionnaireEntity.getSize() + 1) + (pageQuestionnaireEntity.getContent().size() - 1))
                    .setTotalRecord(pageQuestionnaireEntity.getTotalElements())
                    .setTotalPage(pageQuestionnaireEntity.getTotalPages())
                    .setPageSize(pageQuestionnaireEntity.getSize())
                    .setCurrentPage(pageQuestionnaireEntity.getNumber() + 1);
        }
        return paginationResDto.setData(questionnaireEntities);
    }

    @PreAuthorize("hasAuthority('QUESTIONNAIRE_U')")
    public QuestionnaireRes update(QuestionnaireReq questionnaireReqDto) {
        LOG.info("----- Updating Questionnaire. -----");

        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionnaireReqDto.getId());
        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE004"));

        QuestionnaireEntity questionnaireEntity = optionalQuestionnaireEntity.get();

        if (questionnaireReqDto.getTitle() != null && !questionnaireReqDto.getTitle().equalsIgnoreCase(questionnaireEntity.getTitle())
                && questionnaireRepo.findByTitle(questionnaireReqDto.getTitle()) != null) {
            throw new CustomException("QUE006");
        }

        if (questionnaireReqDto.getQuestionQuestionnaires() != null) {

            //Add Question sent from frontend
            List<QuestionQuestionnaireEntity> newQuestionQuestionnaires = new ArrayList<>();
            List<QuestionQuestionnaireEntity> existingQuestionQuestionnaires = new ArrayList<>();

            for (QuestionQuestionnaireReq questionQuestionnaireReq : questionnaireReqDto.getQuestionQuestionnaires()) {
                if (questionQuestionnaireReq.getId() != null) {
                    Optional<QuestionQuestionnaireEntity> optionalQuestionQuestionnaireEntity = questionQuestionnaireRepo.findById(questionQuestionnaireReq.getId());
                    optionalQuestionQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE007"));

                    existingQuestionQuestionnaires.add(optionalQuestionQuestionnaireEntity.get());

                    if (questionQuestionnaireReq.getDisplayOrder() != null &&
                            (optionalQuestionQuestionnaireEntity.get().getDisplayOrder() == null
                                    || !questionQuestionnaireReq.getDisplayOrder().equals(optionalQuestionQuestionnaireEntity.get().getDisplayOrder())
                            )
                    ) {
                        optionalQuestionQuestionnaireEntity.get().setDisplayOrder(questionQuestionnaireReq.getDisplayOrder());
                    }
                    if (questionQuestionnaireReq.getQuestionId() != null &&
                            !questionQuestionnaireReq.getQuestionId().equals(optionalQuestionQuestionnaireEntity.get().getQuestion().getId())) {
                        Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(questionQuestionnaireReq.getQuestionId());
                        optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));
                        optionalQuestionQuestionnaireEntity.get().setQuestion(optionalQuestionEntity.get());
                    }
                } else {
                    QuestionQuestionnaireEntity questionQuestionnaire = new QuestionQuestionnaireEntity();
                    Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(questionQuestionnaireReq.getQuestionId());
                    optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));
                    questionQuestionnaire
                            .setQuestion(optionalQuestionEntity.get())
                            .setQuestionnaire(questionnaireEntity)
                            .setDisplayOrder(questionQuestionnaireReq.getDisplayOrder());
                    newQuestionQuestionnaires.add(questionQuestionnaire);
                }
            }
            questionnaireEntity.getQuestionQuestionnaires().addAll(newQuestionQuestionnaires);

            //Remove Question
            List<QuestionQuestionnaireEntity> questionQuestionnaireToRemove = new ArrayList<>();
            for (QuestionQuestionnaireEntity questionQuestionnaireEntity : questionnaireEntity.getQuestionQuestionnaires()) {
                if (!existingQuestionQuestionnaires.contains(questionQuestionnaireEntity) && questionQuestionnaireEntity.getId() != null) {
                    questionQuestionnaireToRemove.add(questionQuestionnaireEntity);
                }
            }
            questionnaireEntity.getQuestionQuestionnaires().removeAll(questionQuestionnaireToRemove);
        }

        dtoUtil.setUpdatedValue(questionnaireReqDto, questionnaireEntity);
        return dtoUtil.prepRes(questionnaireEntity);
    }

    @PreAuthorize("hasAuthority('QUESTIONNAIRE_D')")
    public void delete(Long id) {
        LOG.info("----- Deleting Questionnaire. -----");

        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(id);
        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));

        if (optionalQuestionnaireEntity.get().getQuestionQuestionnaires() != null && optionalQuestionnaireEntity.get().getQuestionQuestionnaires().size() > 0) {
            throw new CustomException("QUE008");
        }

        questionnaireRepo.delete(optionalQuestionnaireEntity.get());
    }
}
