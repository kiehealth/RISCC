package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.AnswerReqContainer;
import com.chronelab.riscc.dto.response.AnswerRes;
import com.chronelab.riscc.service.AnswerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.hibernate.id.SequenceMismatchStrategy.LOG;

@RestController
@RequestMapping(value = "/api/answer")
@Api(tags = "Answer", description = "APIs related to Answer")
public class AnswerController {

    private final AnswerService answerService;
    private final ServiceResponseUtil<AnswerRes> answerResServiceResponseUtil;

    @Autowired
    public AnswerController(AnswerService answerService, ServiceResponseUtil<AnswerRes> answerResServiceResponseUtil) {
        this.answerService = answerService;
        this.answerResServiceResponseUtil = answerResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save bulk answers.")
    public ResponseEntity<ServiceResponse> save(@RequestBody AnswerReqContainer answerContainer) {
        answerService.save(answerContainer);
        return ResponseEntity.ok(new ServiceResponse().setStatus(true).setMessage("Answers saved."));
    }

    @GetMapping
    @ApiOperation(value = "Get all or paginated list of Answers by User. If pagination detail is not provided then all data will be fetched.")
    public ResponseEntity<ServiceResponse> getByUser(PaginationReqDto pagination,
                                                     @RequestParam(required = false) ArrayList<Long> userIds,
                                                     @RequestParam(required = false) ArrayList<Long> questionIds,
                                                     @RequestParam(required = false) ArrayList<Long> questionnaireIds) {

        return ResponseEntity.ok(answerResServiceResponseUtil.buildServiceResponse(
                true,
                "Answers retrieved",
                answerService.get(pagination, userIds, questionIds, questionnaireIds)
                )
        );
    }

    @GetMapping(value = "/by_group_questionnaire/{groupQuestionnaireId}")
    @ApiOperation(value = "Get all or paginated list of Answers by Group Questionnaire. If pagination detail is not provided then all data will be fetched.")
    public ResponseEntity<ServiceResponse> getByUserAndGroupQuestionnaire(PaginationReqDto pagination, @PathVariable Long groupQuestionnaireId) {

        return ResponseEntity.ok(answerResServiceResponseUtil.buildServiceResponse(
                true,
                "Answers retrieved",
                answerService.getByUserAndGroupQuestionnaire(pagination, groupQuestionnaireId)
                )
        );
    }

    @GetMapping(value = "/export/{startDate}/{endDate}")
    @ApiOperation(value = "Export Answer data to csv within date range.")
    public ResponseEntity<ServiceResponse> export(@RequestHeader HttpHeaders httpHeaders,
                                                  @PathVariable Long startDate,//Millisecond UTC
                                                  @PathVariable Long endDate,//Millisecond UTC
                                                  HttpServletResponse response) throws IOException {
        String savedFile = answerService.exportFileWithinDateRange(Integer.parseInt(httpHeaders.get("timeZoneOffsetInMinute").get(0)), startDate, endDate);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .addData("fileUrl", savedFile)
        );
    }
    private static final Logger LOG = LogManager.getLogger();

    @DeleteMapping(value = "/by_user/{userId}")
    public ResponseEntity<ServiceResponse> deleteByUser(@PathVariable Long userId) {
        LOG.info("----- Deleting Answers by User in controller  -----");

        answerService.deleteByUser(userId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Answers deleted permanently.")
        );
    }
}
