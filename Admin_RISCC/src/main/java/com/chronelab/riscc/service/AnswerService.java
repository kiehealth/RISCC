package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.RisccValueCalcualtionDtoUtil;
import com.chronelab.riscc.dto.request.AnswerReq;
import com.chronelab.riscc.dto.request.AnswerReqContainer;
import com.chronelab.riscc.dto.response.AnswerRes;
import com.chronelab.riscc.dto.response.RisccCalculationRes;
import com.chronelab.riscc.dto.util.AnswerDtoUtil;
import com.chronelab.riscc.dto.util.AnswerOptionDtoUtil;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.*;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.*;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.specification.AnswerSpecificationBuilder;
import com.chronelab.riscc.repo.specification.SearchCriteria;
import com.chronelab.riscc.util.CsvUtil;
import com.chronelab.riscc.util.EmailMessage;
import com.chronelab.riscc.util.GeneralUtil;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnswerService {

    private static final Logger LOG = LogManager.getLogger();

    private final UserRepo userRepo;
    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;
    private final QuestionOptionRepo questionOptionRepo;
    private final QuestionnaireRepo questionnaireRepo;
    private final GroupQuestionnaireRepo groupQuestionnaireRepo;
    private final GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo;
    private final DtoUtil<AnswerEntity, AnswerReq, AnswerRes> dtoUtil;
    private final PaginationDtoUtil<AnswerEntity, AnswerReq, AnswerRes> paginationDtoUtil;
    private final AnswerOptionDtoUtil answerOptionDtoUtil;
    private final EmailService emailService;
    private final CsvUtil csvUtil;
    private final RisccValueCalcualtionDtoUtil risccValueCalcualtionDtoUtil;


    @Autowired
    public AnswerService(UserRepo userRepo, AnswerRepo answerRepo,
                         QuestionRepo questionRepo, QuestionOptionRepo questionOptionRepo, GroupQuestionnaireRepo groupQuestionnaireRepo, GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo, AnswerDtoUtil answerDtoUtil,
                         QuestionnaireRepo questionnaireRepo, PaginationDtoUtil<AnswerEntity, AnswerReq, AnswerRes> paginationDtoUtil, AnswerOptionDtoUtil answerOptionDtoUtil, EmailServiceImpl emailServiceImpl, CsvUtil csvUtil, RisccValueCalcualtionDtoUtil risccValueCalcualtionDtoUtil) {
        this.userRepo = userRepo;
        this.answerRepo = answerRepo;
        this.questionRepo = questionRepo;
        this.questionOptionRepo = questionOptionRepo;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
        this.groupQuestionnaireAnswerFinishedRepo = groupQuestionnaireAnswerFinishedRepo;
        this.dtoUtil = answerDtoUtil;
        this.questionnaireRepo = questionnaireRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.answerOptionDtoUtil = answerOptionDtoUtil;
        this.emailService = emailServiceImpl;
        this.csvUtil = csvUtil;
        this.risccValueCalcualtionDtoUtil = risccValueCalcualtionDtoUtil;
    }

    public void save(AnswerReqContainer answerReqContainer) {
        LOG.info("----- Saving Answers. -----");

        List<AnswerReq> answerReqs = answerReqContainer.getAnswers();

        if (answerReqs != null && answerReqs.size() > 0) {
            Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
            optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

            Optional<GroupQuestionnaireEntity> optionalGroupQuestionnaireEntity = groupQuestionnaireRepo.findById(answerReqContainer.getGroupQuestionnaireId());
            optionalGroupQuestionnaireEntity.orElseThrow(() -> new CustomException("GRP003"));

            List<GroupQuestionnaireAnswerFinishedEntity> groupQuestionnaireAnswerFinishedEntities = groupQuestionnaireAnswerFinishedRepo.findAllByUserAndGroupQuestionnaireAndFinishedDateTimeBetween(
                    optionalUserEntity.get(), optionalGroupQuestionnaireEntity.get(), optionalGroupQuestionnaireEntity.get().getStartDateTime(), optionalGroupQuestionnaireEntity.get().getEndDateTime()
            );
            if (groupQuestionnaireAnswerFinishedEntities != null && groupQuestionnaireAnswerFinishedEntities.size() > 0) {
                throw new CustomException("GRP004");
            }

            Map<String, String[]> emailMessageUser = new HashMap<>();//(email, [message, questionTitle])
            Map<String, String[]> emailMessageOther = new HashMap<>();//(email, [message, questionTitle])

            List<AnswerEntity> answers = new ArrayList<>();
            for (AnswerReq answerReq : answerReqs) {
                Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(answerReq.getQuestionId());
                optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));

                /*if (answerRepo.existsByUserAndQuestionAndGroupQuestionnaire(optionalUserEntity.get(), optionalQuestionEntity.get(), optionalGroupQuestionnaireEntity.get())) {
                    throw new CustomException("QUE003");
                }*/
                if (answerRepo.existsByUserAndQuestionAndGroupQuestionnaireAndCreatedDateBetween(optionalUserEntity.get(), optionalQuestionEntity.get(), optionalGroupQuestionnaireEntity.get(),
                        optionalGroupQuestionnaireEntity.get().getStartDateTime(), optionalGroupQuestionnaireEntity.get().getEndDateTime())) {
                    throw new CustomException("QUE003");
                }

                if ((answerReq.getAnswer() == null || answerReq.getAnswer().isEmpty())
                        && (answerReq.getAnswerOptions() == null || answerReq.getAnswerOptions().size() == 0)) {
                    throw new CustomException("QUE004");
                }

                AnswerEntity answer = dtoUtil.reqToEntity(answerReq)
                        .setQuestion(optionalQuestionEntity.get())
                        .setUser(optionalUserEntity.get())
                        .setGroupQuestionnaire(optionalGroupQuestionnaireEntity.get());

                if (answerReq.getAnswerOptions() != null) {
                    List<AnswerOptionEntity> answerOptions = answerReq.getAnswerOptions().stream().map(answerOptionReqDto -> {
                        Optional<QuestionOptionEntity> optionalQuestionOptionEntity = questionOptionRepo.findById(answerOptionReqDto.getQuestionOptionId());
                        optionalQuestionOptionEntity.orElseThrow(() -> new CustomException("QUE005"));

                        if (optionalQuestionOptionEntity.get().getOptionMessage() != null && !optionalQuestionOptionEntity.get().getOptionMessage().isEmpty()) {
                            if (optionalQuestionOptionEntity.get().getNotifyUser() != null && optionalQuestionOptionEntity.get().getNotifyUser()) {
                                emailMessageUser.put(optionalUserEntity.get().getEmailAddress(), new String[]{optionalQuestionOptionEntity.get().getOptionMessage(), optionalQuestionEntity.get().getTitle()});
                            }
                            if (optionalQuestionOptionEntity.get().getNotifyOther() != null && optionalQuestionOptionEntity.get().getNotifyOther()
                                    && optionalQuestionOptionEntity.get().getOtherEmail() != null) {
                                emailMessageOther.put(optionalQuestionOptionEntity.get().getOtherEmail(), new String[]{optionalQuestionOptionEntity.get().getOptionMessage(), optionalQuestionEntity.get().getTitle()});
                            }
                        }

                        AnswerOptionEntity answerOption = answerOptionDtoUtil.reqToEntity(answerOptionReqDto)
                                .setValue(optionalQuestionOptionEntity.get().getTitle());
                        answerOption.setQuestionOption(optionalQuestionOptionEntity.get());
                        answerOption.setAnswer(answer);
                        return answerOption;
                    }).collect(Collectors.toList());
                    answer.setAnswerOptions(answerOptions);

                }
                answers.add(answer);
            }

            answerRepo.saveAll(answers);

            //Send email to user
            if (emailMessageUser.size() > 0) {
                for (Map.Entry<String, String[]> entrySet : emailMessageUser.entrySet()) {
                    String emailBody = EmailMessage.answerOptionMessage(entrySet.getValue()[0], null, null, null);
                    emailService.sendFormattedEmail(entrySet.getKey(), "Answer for question: " + entrySet.getValue()[1], emailBody);
                }
            }

            //Send email to other
            if (emailMessageOther.size() > 0) {
                for (Map.Entry<String, String[]> entrySet : emailMessageOther.entrySet()) {
                    String emailBody = EmailMessage.answerOptionMessage(entrySet.getValue()[0],
                            optionalUserEntity.get().getFirstName() + " " + optionalUserEntity.get().getLastName(),
                            optionalUserEntity.get().getEmailAddress(), optionalUserEntity.get().getMobileNumber());
                    emailService.sendFormattedEmail(entrySet.getKey(), "Answer for question: " + entrySet.getValue()[1], emailBody);
                }
            }
        }
    }

    @PreAuthorize("hasAuthority('ANSWER_RA')")
    public PaginationResDto<AnswerRes> get(PaginationReqDto paginationReqDto, List<Long> userIds, List<Long> questionIds, List<Long> questionnaireIds) {
        LOG.info("----- Getting Answers. -----");

        List<String> fields = Arrays.asList("createdDate", "answer", "questionResearchId", "question", "user","dateTime");
        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && fields.contains(paginationReqDto.getSortBy())
                && paginationReqDto.getSortOrder() != null) {
            if (paginationReqDto.getSortBy().equals("dateTime")) {
                sortBy = "createdDate";
            } else if (paginationReqDto.getSortBy().equals("question")) {
                sortBy = "question.title";
            } else if (paginationReqDto.getSortBy().equals("user")) {
                sortBy = "user.firstName";
            } else {
                sortBy = paginationReqDto.getSortBy();
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (userIds != null) {
            searchCriteriaList.add(new SearchCriteria("userIds", ":", userIds.stream().map(userId -> {
                        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                        return optionalUserEntity.get();
                    }).collect(Collectors.toList()), false)
            );
        }
        if (questionIds != null) {
            searchCriteriaList.add(new SearchCriteria("questionIds", ":", questionIds.stream().map(questionId -> {
                        Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(questionId);
                optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));
                        return optionalQuestionEntity.get();
                    }).collect(Collectors.toList()), false)
            );
        }
        if (questionnaireIds != null) {
            searchCriteriaList.add(new SearchCriteria("questionnaireIds", ":", questionnaireIds.stream().map(questionnaireId -> {
                        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionnaireId);
                optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                        return optionalQuestionnaireEntity.get();
                    }).collect(Collectors.toList()), false)
            );
        }

        if (paginationReqDto.getSearchTerm() != null && !paginationReqDto.getSearchTerm().isEmpty()) {
            searchCriteriaList.add(new SearchCriteria("answer", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("questionTitle", ":", paginationReqDto.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("questionBody", ":", paginationReqDto.getSearchTerm(), true));
        }

        Page<AnswerEntity> answerEntityPage = null;
        List<AnswerEntity> answerEntities;
        if (paginationReqDto.getPageSize() > 0) {
            answerEntityPage = answerRepo.findAll(new AnswerSpecificationBuilder().with(searchCriteriaList).build(),
                    PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            answerEntities = answerEntityPage.getContent();
        } else {
            answerEntities = answerRepo.findAll(new AnswerSpecificationBuilder().with(searchCriteriaList).build(),
                    Sort.by(sortOrder, sortBy));
        }

        List<AnswerRes> answerResDtos = answerEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(answerEntityPage, answerResDtos);
    }


    public String exportFileWithinDateRange(int timeZoneOffsetInMinute, Long startDate, Long endDate) throws IOException {
        LOG.info("----- Exporting File with Answer within Date range. -----");

        List<AnswerEntity> answerEntities = answerRepo.findAllByCreatedDateBetween(Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).toLocalDateTime(),
                Instant.ofEpochMilli(endDate).atZone(ZoneOffset.UTC).toLocalDateTime(),
                Sort.by(Sort.Direction.DESC, "createdDate"));

        List<String> headingCsv = Arrays.asList("SN", "Questionnaire", "Name", "Email", "Date", "Title", "Body", "Answer", "Type");

        List<List<String>> dataCsv = new ArrayList<>();
        int j = 1;
        for (AnswerEntity answerEntity : answerEntities) {
            List<String> rowCsv = new ArrayList<>();

            rowCsv.add(String.valueOf(j++));

            rowCsv.add(answerEntity.getGroupQuestionnaire().getQuestionnaire().getTitle());
            rowCsv.add(answerEntity.getUser().getFirstName() + " " + answerEntity.getUser().getLastName());
            rowCsv.add(answerEntity.getUser().getEmailAddress());
            rowCsv.add(GeneralUtil.formatDateTime(LocalDateTime.ofEpochSecond(answerEntity.getCreatedDate().toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.ofTotalSeconds(-timeZoneOffsetInMinute * 60))));

            rowCsv.add(answerEntity.getQuestion().getTitle());

            rowCsv.add(answerEntity.getQuestion().getBody());

            if (answerEntity.getAnswerOptions() != null && answerEntity.getAnswerOptions().size() > 0) {
                String answers = answerEntity.getAnswerOptions().stream().map(AnswerOptionEntity::getValue).collect(Collectors.joining(","));
                rowCsv.add(answers);
            } else {
                rowCsv.add(answerEntity.getAnswer());
            }
            rowCsv.add(GeneralUtil.formatString(answerEntity.getQuestion().getQuestionType().getTitle()));

            dataCsv.add(rowCsv);
        }

        return csvUtil.createCsv("answer_report", headingCsv, dataCsv);
    }

    public PaginationResDto<AnswerRes> getByUserAndGroupQuestionnaire(PaginationReqDto paginationReqDto, Long groupQuestionnaireId) {
        LOG.info("----- Getting Answers for Logged in user by Group Questionnaire. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        Optional<GroupQuestionnaireEntity> optionalGroupQuestionnaireEntity = groupQuestionnaireRepo.findById(groupQuestionnaireId);
        optionalGroupQuestionnaireEntity.orElseThrow(() -> new CustomException("GRP003"));

        String sortBy = "createdDate";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        Page<AnswerEntity> answerEntityPage = null;
        List<AnswerEntity> answerEntities = new ArrayList<>();

        if (paginationReqDto.getPageSize() > 0) {
            answerEntityPage = answerRepo.findAllByUserAndGroupQuestionnaire(optionalUserEntity.get(), optionalGroupQuestionnaireEntity.get(), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            answerEntities = answerEntityPage.getContent();
        } else {
            answerEntities = answerRepo.findAllByUserAndGroupQuestionnaire(optionalUserEntity.get(), optionalGroupQuestionnaireEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<AnswerRes> answerResList = answerEntities.stream().map(dtoUtil::prepRes).collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(answerEntityPage, answerResList);
    }

    @PreAuthorize("hasAuthority('ANSWER_D')")
    public void deleteByUser(Long userId) {
        LOG.info("----- Deleting Answers by User. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        answerRepo.deleteAllByUser(optionalUserEntity.get());
        groupQuestionnaireAnswerFinishedRepo.deleteAllByUser(optionalUserEntity.get());
    }

    @PreAuthorize("hasAuthority('ANSWER_RA')")
    public List<RisccCalculationRes> calculateRiscc(ArrayList<Long> userIds, ArrayList<Long> questionnaireIds) {
        LOG.info("----------- Calculate the riscc value .");
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (userIds != null) {
            searchCriteriaList.add(new SearchCriteria("userIds", ":", userIds.stream().map(userId -> {
                        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
                        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
                        return optionalUserEntity.get();
                    }).collect(Collectors.toList()), false)
            );
        }
        if (questionnaireIds != null) {
            searchCriteriaList.add(new SearchCriteria("questionnaireIds", ":", questionnaireIds.stream().map(questionnaireId -> {
                        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionnaireId);
                        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                        return optionalQuestionnaireEntity.get();
                    }).collect(Collectors.toList()), false)
            );
        }
        List<AnswerEntity> answerEntities = answerRepo.findAll(new AnswerSpecificationBuilder().with(searchCriteriaList).build());

        List<AnswerRes> answerResDtos = answerEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return risccValueCalcualtionDtoUtil.calculateRisccValue(answerResDtos);

    }
}
