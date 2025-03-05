package com.chronelab.riscc.service;

import com.chronelab.riscc.config.SessionManager;
import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.QuestionFileDto;
import com.chronelab.riscc.dto.request.QuestionOptionReq;
import com.chronelab.riscc.dto.request.QuestionQuestionnaireReq;
import com.chronelab.riscc.dto.request.QuestionReq;
import com.chronelab.riscc.dto.response.QuestionRes;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.dto.util.QuestionDtoUtil;
import com.chronelab.riscc.dto.util.QuestionOptionDtoUtil;
import com.chronelab.riscc.entity.*;
import com.chronelab.riscc.entity.general.UserEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.*;
import com.chronelab.riscc.repo.general.UserRepo;
import com.chronelab.riscc.repo.specification.QuestionSpecificationBuilder;
import com.chronelab.riscc.repo.specification.SearchCriteria;
import com.chronelab.riscc.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('QUESTION')")
@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionService {

    private static final Logger LOG = LogManager.getLogger();

    private final QuestionRepo questionRepo;
    private final QuestionTypeRepo questionTypeRepo;
    private final QuestionnaireRepo questionnaireRepo;
    private final QuestionOptionRepo questionOptionRepo;
    private final UserRepo userRepo;
    private final AnswerOptionRepo answerOptionRepo;
    private final QuestionQuestionnaireRepo questionQuestionnaireRepo;
    private final GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo;
    private final GroupQuestionnaireRepo groupQuestionnaireRepo;

    private final DtoUtil<QuestionEntity, QuestionReq, QuestionRes> dtoUtil;
    private final QuestionOptionDtoUtil questionOptionDtoUtil;
    private final PaginationDtoUtil<QuestionEntity, QuestionReq, QuestionRes> paginationDtoUtil;

    @Autowired
    public QuestionService(QuestionRepo questionRepo, QuestionTypeRepo questionTypeRepo,
                           QuestionnaireRepo questionnaireRepo, QuestionDtoUtil questionDtoUtil, QuestionOptionDtoUtil questionOptionDtoUtil,
                           QuestionOptionRepo questionOptionRepo, UserRepo userRepo, AnswerOptionRepo answerOptionRepo,
                           QuestionQuestionnaireRepo questionQuestionnaireRepo, GroupQuestionnaireAnswerFinishedRepo groupQuestionnaireAnswerFinishedRepo, GroupQuestionnaireRepo groupQuestionnaireRepo, PaginationDtoUtil<QuestionEntity, QuestionReq, QuestionRes> paginationDtoUtil) {
        this.questionRepo = questionRepo;
        this.questionTypeRepo = questionTypeRepo;
        this.questionnaireRepo = questionnaireRepo;
        this.dtoUtil = questionDtoUtil;
        this.questionOptionDtoUtil = questionOptionDtoUtil;
        this.questionOptionRepo = questionOptionRepo;
        this.userRepo = userRepo;
        this.answerOptionRepo = answerOptionRepo;
        this.questionQuestionnaireRepo = questionQuestionnaireRepo;
        this.groupQuestionnaireAnswerFinishedRepo = groupQuestionnaireAnswerFinishedRepo;
        this.groupQuestionnaireRepo = groupQuestionnaireRepo;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('QUESTION_C')")
    public QuestionRes save(QuestionReq questionReqDto) {
        LOG.info("----- Saving Question. -----");

        return dtoUtil.prepRes(questionRepo.save(prepareQuestion(questionReqDto)));
    }

    private QuestionEntity prepareQuestion(QuestionReq questionReqDto) {

        List<QuestionnaireEntity> questionnaireEntities = new ArrayList<>();
        questionReqDto.getQuestionQuestionnaires().forEach(questionQuestionnaireReqDto -> {
            Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionQuestionnaireReqDto.getQuestionnaireId());
            optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
            questionnaireEntities.add(optionalQuestionnaireEntity.get());
        });

        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("questionTitle", ":", questionReqDto.getTitle(), false));
        if (questionReqDto.getBody() != null && !questionReqDto.getBody().isEmpty()) {
            searchCriteria.add(new SearchCriteria("questionBody", ":", questionReqDto.getBody(), false));
        }
        if (questionnaireEntities.size() > 0) {
            searchCriteria.add(new SearchCriteria("questionnaireIds", ":", questionnaireEntities, false));
        }
        List<QuestionEntity> questionEntities = questionRepo.findAll(new QuestionSpecificationBuilder().with(searchCriteria).build());

        if (questionEntities.size() > 0) {
            throw new CustomException("QUE009");
        }
        /*
        questionRepo.findByTitleAndBody(questionReqDto.getTitle(), questionReqDto.getBody())
                .ifPresent((questionEntity) -> {
                    throw new CustomException("QUE005");
                });*/

        QuestionEntity question = dtoUtil.reqToEntity(questionReqDto);

        //Question Type
        Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findById(questionReqDto.getQuestionTypeId());
        optionalQuestionTypeEntity.orElseThrow(() -> new CustomException("QUE010"));
        question.setQuestionType(optionalQuestionTypeEntity.get());

        //Questionnaire
        if (questionReqDto.getQuestionQuestionnaires() != null) {
            List<QuestionQuestionnaireEntity> questionQuestionnaires = new ArrayList<>();

            for (QuestionQuestionnaireReq questionQuestionnaireReqDto : questionReqDto.getQuestionQuestionnaires()) {
                if (questionQuestionnaireReqDto.getQuestionnaireId() != null) {
                    QuestionQuestionnaireEntity questionQuestionnaire = new QuestionQuestionnaireEntity();

                    Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionQuestionnaireReqDto.getQuestionnaireId());
                    optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                    questionQuestionnaire.setQuestionnaire(optionalQuestionnaireEntity.get())
                            .setDisplayOrder(questionQuestionnaireReqDto.getDisplayOrder())
                            .setQuestion(question);
                    questionQuestionnaires.add(questionQuestionnaire);
                }
            }
            question.setQuestionQuestionnaires(questionQuestionnaires);
        }

        //Question Option
        if (questionReqDto.getQuestionOptions() != null && questionReqDto.getQuestionOptions().size() > 0) {
            question.setQuestionOptions(questionReqDto.getQuestionOptions().stream().map(
                    (questionOptionReqDto) -> questionOptionDtoUtil.reqToEntity(questionOptionReqDto)
                            .setQuestion(question)
            ).collect(Collectors.toList()));
        }

        return question;
    }

    @Transactional(readOnly = true)
    public PaginationResDto<QuestionRes> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Questions. -----");

        List<String> fields = Arrays.asList("title", "body");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;

        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortOrder() != null) {
            if (paginationReqDto.getSortBy().equalsIgnoreCase("displayOrder")) {
//                sortBy = "questionQuestionnaires.displayOrder";
                sortBy = "title";
            } else {
                sortBy = paginationReqDto.getSortBy();
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<QuestionEntity> questionEntityPage = null;
        List<QuestionEntity> questionEntities = null;

        if (paginationReqDto.getPageSize() > 0) {
            questionEntityPage = questionRepo.findDistinctByTitleNotNull(PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
            questionEntities = questionEntityPage.getContent();
        } else {
            questionEntities = questionRepo.findDistinctByTitleNotNull(Sort.by(sortOrder, sortBy));
        }


        List<QuestionRes> questionResDtos = questionEntities.stream().map(dtoUtil::prepRes).collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(questionEntityPage, questionResDtos);
    }

    public PaginationResDto<QuestionRes> getByQuestionnaire(Long questionnaireId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Question by Questionnaire. -----");

        List<String> fields = Arrays.asList("title","displayOrder");
        //String sortBy = "title";//Default sortBy
        String sortBy = "questionQuestionnaires";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.DESC;//Default sortOrder

        if (paginationReqDto.getSortBy() != null && paginationReqDto.getSortOrder() != null) {
            if (paginationReqDto.getSortBy().equalsIgnoreCase("displayOrder")) {
                sortBy = "questionQuestionnaires";
            } else {
                sortBy = paginationReqDto.getSortBy();
            }
            sortOrder = paginationReqDto.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<QuestionEntity> questionEntityPage = null;
        List<QuestionEntity> questionEntities = new ArrayList<>();

        if (questionnaireId != null) {
            QuestionnaireEntity questionnaireEntity = questionnaireRepo.findById(questionnaireId).orElseThrow(() -> new CustomException("QUE004"));

            if (paginationReqDto.getPageSize() > 0) {
                questionEntityPage = questionRepo.findAllByQuestionQuestionnaires_QuestionnaireIn(Arrays.asList(questionnaireEntity), PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize(), sortOrder, sortBy));
                questionEntities = questionEntityPage.getContent();
            } else {
                questionEntities = questionRepo.findAllByQuestionQuestionnaires_QuestionnaireIn(Arrays.asList(questionnaireEntity), Sort.by(sortOrder, sortBy));
            }
        }

        List<QuestionRes> questionResDtos = questionEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(questionEntityPage, questionResDtos);
    }

    public PaginationResDto<QuestionRes> getUnansweredByQuestionnaire(Long questionnaireId, Long groupQuestionnaireId, PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting Unanswered Question by Questionnaire. -----");

        Page<QuestionEntity> questionEntityPage = null;
        List<QuestionEntity> questionEntities;

        UserEntity userEntity = userRepo.findById(SessionManager.getUserId())
                .orElseThrow(() -> new CustomException("USR001"));

        GroupQuestionnaireEntity groupQuestionnaireEntity = groupQuestionnaireRepo.findById(groupQuestionnaireId)
                .orElseThrow(() -> new CustomException("GRP003"));

        /*boolean fetchAllQuestions = false;
        List<GroupQuestionnaireAnswerFinishedEntity> groupQuestionnaireAnswerFinishedEntities = groupQuestionnaireAnswerFinishedRepo.findAllByUserAndGroupQuestionnaireAndFinishedDateTimeBetween(
                userEntity, groupQuestionnaireEntity, groupQuestionnaireEntity.getStartDateTime(),
                groupQuestionnaireEntity.getEndDateTime()
        );
        if (groupQuestionnaireAnswerFinishedEntities == null || groupQuestionnaireAnswerFinishedEntities.size() == 0) {
            fetchAllQuestions = true;
        }*/


        if (paginationReqDto.getPageSize() > 0) {
            /*if (fetchAllQuestions) {
                return this.getByQuestionnaire(questionnaireId, paginationReqDto);
            } else {
            }*/
            questionEntityPage = questionRepo.unansweredQuestions(questionnaireId, SessionManager.getUserId(), groupQuestionnaireId,
                    groupQuestionnaireEntity.getStartDateTime(), groupQuestionnaireEntity.getEndDateTime(),
                    PageRequest.of(paginationReqDto.getPageNumber(), paginationReqDto.getPageSize()));
            questionEntities = questionEntityPage.getContent();
        } else {
            /*if (fetchAllQuestions) {
                return this.getByQuestionnaire(questionnaireId, paginationReqDto);
            } else {
            }*/
            questionEntities = questionRepo.unansweredQuestions(questionnaireId, SessionManager.getUserId(), groupQuestionnaireId,
                    groupQuestionnaireEntity.getStartDateTime(), groupQuestionnaireEntity.getEndDateTime());
        }


        List<QuestionRes> questionResDtos = questionEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(questionEntityPage, questionResDtos);
    }

    @PreAuthorize("hasAuthority('QUESTION_U')")
    public QuestionRes update(QuestionReq questionReqDto) {
        LOG.info("----- Updating Question. -----");

        Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(questionReqDto.getId());
        optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));

        QuestionEntity questionEntity = optionalQuestionEntity.get();

        //Question Type
        if (questionReqDto.getQuestionTypeId() != null && !questionReqDto.getQuestionTypeId().equals(questionEntity.getQuestionType().getId())) {
            Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findById(questionReqDto.getQuestionTypeId());
            optionalQuestionTypeEntity.orElseThrow(() -> new CustomException("QUE010"));
            questionEntity.setQuestionType(optionalQuestionTypeEntity.get());
            if (!optionalQuestionTypeEntity.get().getTitle().equalsIgnoreCase("SELECT_MORE_THAN_ONE")
                    && !optionalQuestionTypeEntity.get().getTitle().equalsIgnoreCase("SELECT_ONE")
                    && !optionalQuestionTypeEntity.get().getTitle().equalsIgnoreCase("RATING")) {
                questionEntity.getQuestionOptions().clear();
            }
        }

        //Questionnaire
        if (questionReqDto.getQuestionQuestionnaires() != null) {

            //Add Questionnaire sent from frontend
            List<QuestionQuestionnaireEntity> newQuestionQuestionnaires = new ArrayList<>();
            List<QuestionQuestionnaireEntity> existingQuestionQuestionnaires = new ArrayList<>();

            for (QuestionQuestionnaireReq questionQuestionnaireReqDto : questionReqDto.getQuestionQuestionnaires()) {
                if (questionQuestionnaireReqDto.getId() != null) {
                    Optional<QuestionQuestionnaireEntity> optionalQuestionQuestionnaireEntity = questionQuestionnaireRepo.findById(questionQuestionnaireReqDto.getId());
                    optionalQuestionQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE007"));

                    existingQuestionQuestionnaires.add(optionalQuestionQuestionnaireEntity.get());

                    if (questionQuestionnaireReqDto.getDisplayOrder() != null &&
                            (optionalQuestionQuestionnaireEntity.get().getDisplayOrder() == null
                                    || !questionQuestionnaireReqDto.getDisplayOrder().equals(optionalQuestionQuestionnaireEntity.get().getDisplayOrder())
                            )
                    ) {
                        optionalQuestionQuestionnaireEntity.get().setDisplayOrder(questionQuestionnaireReqDto.getDisplayOrder());
                    }
                    if (questionQuestionnaireReqDto.getQuestionnaireId() != null &&
                            !questionQuestionnaireReqDto.getQuestionnaireId().equals(optionalQuestionQuestionnaireEntity.get().getQuestionnaire().getId())) {
                        Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionQuestionnaireReqDto.getQuestionnaireId());
                        optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                        optionalQuestionQuestionnaireEntity.get().setQuestionnaire(optionalQuestionnaireEntity.get());
                    }
                } else {
                    QuestionQuestionnaireEntity questionQuestionnaire = new QuestionQuestionnaireEntity();
                    Optional<QuestionnaireEntity> optionalQuestionnaireEntity = questionnaireRepo.findById(questionQuestionnaireReqDto.getQuestionnaireId());
                    optionalQuestionnaireEntity.orElseThrow(() -> new CustomException("QUE001"));
                    questionQuestionnaire
                            .setQuestionnaire(optionalQuestionnaireEntity.get())
                            .setQuestion(questionEntity)
                            .setDisplayOrder(questionQuestionnaireReqDto.getDisplayOrder());
                    newQuestionQuestionnaires.add(questionQuestionnaire);
                }
            }
            questionEntity.getQuestionQuestionnaires().addAll(newQuestionQuestionnaires);

            //Remove Questionnaire
            List<QuestionQuestionnaireEntity> questionQuestionnaireToRemove = new ArrayList<>();
            for (QuestionQuestionnaireEntity questionQuestionnaireEntity : questionEntity.getQuestionQuestionnaires()) {
                if (!existingQuestionQuestionnaires.contains(questionQuestionnaireEntity) && questionQuestionnaireEntity.getId() != null) {
                    questionQuestionnaireToRemove.add(questionQuestionnaireEntity);
                }
            }
            questionEntity.getQuestionQuestionnaires().removeAll(questionQuestionnaireToRemove);
        }

        //Question Options
        if (questionReqDto.getQuestionOptions() != null) {
            List<QuestionOptionEntity> existing = questionEntity.getQuestionOptions() != null ? questionEntity.getQuestionOptions() : new ArrayList<>();
            List<QuestionOptionEntity> toAdd = new ArrayList<>();
            List<QuestionOptionEntity> toRemove = new ArrayList<>();

            for (QuestionOptionReq questionOptionReqDto : questionReqDto.getQuestionOptions()) {
                if (questionOptionReqDto.getId() != null) {//Update existing Question Option
                    Optional<QuestionOptionEntity> optionalQuestionOptionEntity = questionOptionRepo.findById(questionOptionReqDto.getId());
                    optionalQuestionOptionEntity.orElseThrow(() -> new CustomException("QUE005"));
                    questionOptionDtoUtil.setUpdatedValue(questionOptionReqDto, optionalQuestionOptionEntity.get());
                } else {//Add new Question Option
                    toAdd.add(((QuestionOptionEntity) questionOptionDtoUtil.reqToEntity(questionOptionReqDto)).setQuestion(questionEntity));
                }
            }

            //Remove Question Option
            for (QuestionOptionEntity questionOptionEntity : existing) {
                boolean delete = true;
                for (QuestionOptionReq questionOptionReqDto : questionReqDto.getQuestionOptions()) {
                    if (questionOptionReqDto.getId() != null && questionOptionReqDto.getId().equals(questionOptionEntity.getId())) {
                        delete = false;
                        break;
                    }
                }
                if (delete) {
                    toRemove.add(questionOptionEntity);
                }
            }
            for (QuestionOptionEntity questionOptionEntity : toRemove) {
                if (questionOptionEntity.getAnswerOptions() != null && questionOptionEntity.getAnswerOptions().size() > 0) {
                    throw new CustomException("QUE011");
                }
            }

            questionEntity.getQuestionOptions().removeAll(toRemove);
            questionEntity.getQuestionOptions().addAll(toAdd);
        }

        dtoUtil.setUpdatedValue(questionReqDto, questionEntity);

        return dtoUtil.prepRes(questionEntity);
    }

    public void delete(Long id) {
        LOG.info("----- Deleting question. -----");

        Optional<QuestionEntity> optionalQuestionEntity = questionRepo.findById(id);
        optionalQuestionEntity.orElseThrow(() -> new CustomException("QUE002"));

        questionRepo.delete(optionalQuestionEntity.get());
    }

    public void deleteQuestionOption(Long questionOptionId) {
        LOG.info("----- Deleting Question Option. -----");

        Optional<QuestionOptionEntity> optionalQuestionOptionEntity = questionOptionRepo.findById(questionOptionId);
        optionalQuestionOptionEntity.orElseThrow(() -> new CustomException("QUE005"));

        if (optionalQuestionOptionEntity.get().getAnswerOptions() != null && optionalQuestionOptionEntity.get().getAnswerOptions().size() > 0) {
            answerOptionRepo.deleteAll(optionalQuestionOptionEntity.get().getAnswerOptions());
        }

        questionOptionRepo.delete(optionalQuestionOptionEntity.get());
    }

    public boolean importQuestionData(MultipartFile multipartFile) throws IOException {
        LOG.info("----- Importing Question Data. -----");

        List<QuestionFileDto> questionFileDtos = extractDataFromExcelFile(multipartFile);
        List<QuestionFileDto> uniqueQuestionFileDtos = removeDuplicate(questionFileDtos);
        List<QuestionReq> questionReqDtos = getQuestionReq(uniqueQuestionFileDtos);
        List<QuestionEntity> questions = questionReqDtos.stream().map(this::prepareQuestion).collect(Collectors.toList());
        return questionRepo.saveAll(questions).size() > 0;
    }

    private List<QuestionFileDto> extractDataFromExcelFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("catalina.home") + File.separator + "saved_file" + File.separator + multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        List<QuestionFileDto> questionFileDtos = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            QuestionFileDto questionFileDto = new QuestionFileDto();
            List<String> options = new ArrayList<>();
            for (short j = 0; j <= sheet.getRow(i).getLastCellNum(); j++) {
                String value = dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
                switch (j) {
                    case 0:
                        if (value == null || value.isEmpty()) {
                            throw new CustomException("QUE001", i + 1);
                        }
                        questionFileDto.setQuestionnaire(value);
                        break;
                    case 1:
                        if (value == null || value.isEmpty()) {
                            throw new CustomException("QUE010", i + 1);
                        }
                        questionFileDto.setQuestionType(value);
                        break;
                    case 2:
                        if (value == null || value.isEmpty()) {
                            throw new CustomException("QUE012", i + 1);
                        }
                        questionFileDto.setTitle(value);
                        break;
                    case 3:
                        if (value == null || value.isEmpty()) {
                            throw new CustomException("QUE013", i + 1);
                        }
                        questionFileDto.setBody(value);
                        break;
                    default:
                        options.add(value);
                }

            }
            questionFileDto.setOptions(options);
            questionFileDtos.add(questionFileDto);
        }

        workbook.close();

        if (file.exists()) {
            if (file.delete()) {
                LOG.info("----- Temporary Excel file deleted. -----");
            }
        }
        return questionFileDtos;
    }

    private List<QuestionFileDto> removeDuplicate(List<QuestionFileDto> questionFileDtos) {
        List<QuestionFileDto> uniqueQuestionFileDtos = new ArrayList<>();
        for (QuestionFileDto questionFileDto : questionFileDtos) {
            boolean exists = false;
            for (QuestionFileDto unique : uniqueQuestionFileDtos) {
                if (unique.getTitle().equalsIgnoreCase(questionFileDto.getTitle())) {
                    exists = true;
                }
            }
            if (!exists) {
                uniqueQuestionFileDtos.add(questionFileDto);
            }
        }
        return uniqueQuestionFileDtos;
    }

    private List<QuestionReq> getQuestionReq(List<QuestionFileDto> questionFileDtos) {
        List<QuestionReq> questionReqDtos = new ArrayList<>();

        for (QuestionFileDto questionFileDto : questionFileDtos) {

            QuestionReq questionReqDto = new QuestionReq();

            //Questionnaire
            QuestionnaireEntity questionnaireEntity = questionnaireRepo.findByTitle(questionFileDto.getQuestionnaire());
            if (questionnaireEntity == null) {
                throw new CustomException("QUE001");
            }
            QuestionQuestionnaireReq questionQuestionnaireReqDto = new QuestionQuestionnaireReq();
            questionQuestionnaireReqDto.setQuestionnaireId(questionnaireEntity.getId());
            questionReqDto.setQuestionQuestionnaires(Arrays.asList(questionQuestionnaireReqDto));

            //Question Type
            Optional<QuestionTypeEntity> optionalQuestionTypeEntity = questionTypeRepo.findByTitle(questionFileDto.getQuestionType());
            optionalQuestionTypeEntity.orElseThrow(() -> new CustomException("QUE010"));

            questionReqDto.setQuestionTypeId(optionalQuestionTypeEntity.get().getId());

            questionReqDto.setTitle(questionFileDto.getTitle());
            questionReqDto.setBody(questionFileDto.getBody());

            //Question Option
            if (questionFileDto.getOptions() != null) {
                List<QuestionOptionReq> questionOptionReqDtos = questionFileDto.getOptions().stream().map(option -> {
                    QuestionOptionReq questionOptionReqDto = new QuestionOptionReq();
                    String[] splitted = option.split("#");
                    questionOptionReqDto.setTitle(splitted[0]);
                    if (splitted.length > 1) {
                        questionOptionReqDto.setValue(splitted[1]);
                    }
                    if (splitted.length > 2) {
                        questionOptionReqDto.setResearchId(splitted[2]);
                    }
                    return questionOptionReqDto;
                }).collect(Collectors.toList());
                questionReqDto.setQuestionOptions(questionOptionReqDtos);
            }

            questionReqDtos.add(questionReqDto);
        }
        return questionReqDtos;
    }

    public String downloadTemplateFile() throws IOException {
        LOG.info("----- Downloading Template file. -----");
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Questions");

        Row header = sheet.createRow(0);

        Cell cellQuestionnaire = header.createCell(0);
        cellQuestionnaire.setCellValue("Questionnaire");

        Cell cellQuestionType = header.createCell(1);
        cellQuestionType.setCellValue("QuestionType");

        Cell cellTitle = header.createCell(2);
        cellTitle.setCellValue("Title");

        Cell cellBody = header.createCell(3);
        cellBody.setCellValue("Body");

        Cell cellOption1 = header.createCell(4);
        cellOption1.setCellValue("Option1");

        Cell cellOption2 = header.createCell(5);
        cellOption2.setCellValue("Option2");

        Cell cellOption3 = header.createCell(6);
        cellOption3.setCellValue("Option3");

        Cell cellOption4 = header.createCell(7);
        cellOption4.setCellValue("Option4");

        Cell cellOption5 = header.createCell(8);
        cellOption5.setCellValue("Option5");

        Cell cellOption6 = header.createCell(9);
        cellOption6.setCellValue("Option6");

        return FileUtil.saveExcelFileLocally(workbook, "templates", "question_template", "xlsx");
    }
}
