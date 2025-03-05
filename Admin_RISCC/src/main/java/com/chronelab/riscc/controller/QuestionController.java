package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.QuestionReq;
import com.chronelab.riscc.dto.response.QuestionRes;
import com.chronelab.riscc.service.QuestionService;
import com.chronelab.riscc.service.WelcomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/question")
@Api(tags = "Question", description = "APIs related to Question.")
public class QuestionController {

    private final QuestionService questionService;
    private final WelcomeService welcomeService;
    private final ServiceResponseUtil<QuestionRes> questionResServiceResponseUtil;

    @Autowired
    public QuestionController(QuestionService questionService, WelcomeService welcomeService, ServiceResponseUtil<QuestionRes> questionResServiceResponseUtil) {
        this.questionService = questionService;
        this.welcomeService = welcomeService;
        this.questionResServiceResponseUtil = questionResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Question.")
    public ResponseEntity<ServiceResponse> save(@RequestBody QuestionReq question) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Question saved.")
                        .addData("question", questionService.save(question))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Question list. If pagination detail is not provided then all the Questions will be fetched.")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(questionResServiceResponseUtil.buildServiceResponse(
                true,
                "Questions retrieved",
                questionService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Question.")
    public ResponseEntity<ServiceResponse> update(@RequestBody QuestionReq question) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Question updated.")
                        .addData("question", questionService.update(question))
        );
    }

    @DeleteMapping(value = "/{questionId}")
    @ApiOperation(value = "Delete an existing Question.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Question deleted.")
        );
    }

    @DeleteMapping(value = "/question_option/{questionOptionId}")
    @ApiOperation(value = "Delete an existing Question Option along with its associated answers.")
    public ResponseEntity<ServiceResponse> deleteQuestionOption(@PathVariable Long questionOptionId) {
        questionService.deleteQuestionOption(questionOptionId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Question option deleted.")
        );
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Question data from excel file.")
    public ResponseEntity<ServiceResponse> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        questionService.importQuestionData(multipartFile);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for question.")
    public ResponseEntity<ServiceResponse> export() throws IOException {
        String savedFile = questionService.downloadTemplateFile();
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .addData("fileUrl", savedFile)
        );
    }

    @GetMapping(value = "/by_questionnaire")
    @ApiOperation(value = "Get paginated list or all Question list by Questionnaire. If pagination detail is not provided then all the data will be fetched.")
    public ResponseEntity<ServiceResponse> getByQuestionnaire(@RequestParam(value = "questionnaireId", required = false) Long questionnaireId,
                                                              PaginationReqDto pagination) {

        return ResponseEntity.ok(questionResServiceResponseUtil.buildServiceResponse(
                true,
                "Questions retrieved",
                questionService.getByQuestionnaire(questionnaireId, pagination)
                )
        );
    }

    @GetMapping(value = "/unanswered_by_questionnaire")
    @ApiOperation(value = "Get paginated list or all Unanswered Question list by Questionnaire. If pagination detail is not provided then all the data will be fetched.")
    public ResponseEntity<ServiceResponse> getUnansweredByQuestionnaire(
            @RequestParam(value = "questionnaireId", required = false) Long questionnaireId,
            @RequestParam(value = "groupQuestionnaireId", required = false) Long groupQuestionnaireId,
            PaginationReqDto pagination) {
        return ResponseEntity.ok(questionResServiceResponseUtil.buildServiceResponse(
                true,
                "Questions retrieved",
                questionService.getUnansweredByQuestionnaire(questionnaireId, groupQuestionnaireId, pagination)
                ).addData("welcome", welcomeService.getByLanguage())
        );
    }
}
