package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.QuestionnaireReq;
import com.chronelab.riscc.dto.response.QuestionnaireRes;
import com.chronelab.riscc.repo.projection.QuestionnaireProjection;
import com.chronelab.riscc.service.QuestionnaireService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/questionnaire")
@Api(tags = "Questionnaire", description = "APIs related to Questionnaire.")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final ServiceResponseUtil<QuestionnaireRes> questionnaireResServiceResponseUtil;
    private final ServiceResponseUtil<QuestionnaireProjection> questionnaireProjectionServiceResponseUtil;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService, ServiceResponseUtil<QuestionnaireRes> questionnaireResServiceResponseUtil, ServiceResponseUtil<QuestionnaireProjection> questionnaireProjectionServiceResponseUtil) {
        this.questionnaireService = questionnaireService;
        this.questionnaireResServiceResponseUtil = questionnaireResServiceResponseUtil;
        this.questionnaireProjectionServiceResponseUtil = questionnaireProjectionServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Questionnaire.")
    public ResponseEntity<ServiceResponse> save(@RequestBody QuestionnaireReq questionnaire) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Questionnaire saved.")
                        .addData("questionnaire", questionnaireService.save(questionnaire))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Questionnaire list. If pagination detail is not provided then all the Questionnaire will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(questionnaireResServiceResponseUtil.buildServiceResponse(
                true,
                "Questionnaires retrieved",
                questionnaireService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/fields")
    @ApiOperation(value = "Get paginated list or all Questionnaire list. If pagination detail is not provided then all the Questionnaire will be fetched..")
    public ResponseEntity<ServiceResponse> getLimited(PaginationReqDto pagination,
                                                      @RequestParam(required = false) List<String> fields) {

        return ResponseEntity.ok(questionnaireProjectionServiceResponseUtil.buildServiceResponse(
                true,
                "Questionnaires retrieved",
                questionnaireService.getLimited(pagination, fields)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Questionnaire.")
    public ResponseEntity<ServiceResponse> update(@RequestBody QuestionnaireReq questionnaire) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Questionnaire updated.")
                        .addData("questionnaire", questionnaireService.update(questionnaire))
        );
    }

    @DeleteMapping(value = "/{questionnaireId}")
    @ApiOperation(value = "Delete an existing Questionnaire.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long questionnaireId) {
        questionnaireService.delete(questionnaireId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Questionnaire deleted.")
        );
    }
}
