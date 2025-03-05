package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.QuestionTypeReq;
import com.chronelab.riscc.dto.response.QuestionTypeRes;
import com.chronelab.riscc.service.QuestionTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/question_type")
@Api(tags = "Question Type")
public class QuestionTypeController {

    private final QuestionTypeService questionTypeService;
    private final ServiceResponseUtil<QuestionTypeRes> serviceResponseUtil;

    @Autowired
    public QuestionTypeController(QuestionTypeService questionTypeService, ServiceResponseUtil<QuestionTypeRes> serviceResponseUtil) {
        this.questionTypeService = questionTypeService;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody QuestionTypeReq questionType) {

        return ResponseEntity.ok(
                new ServiceResponse(true, "Question Type saved.")
                        .addData("questionType", questionTypeService.save(questionType))
        );
    }

    @GetMapping
    public ResponseEntity<?> get(PaginationReqDto pagination) {
        return ResponseEntity.ok(
                serviceResponseUtil.buildServiceResponse(
                        true, "Question Type retrieved.", questionTypeService.get(pagination)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody QuestionTypeReq questionType) {

        return ResponseEntity.ok(
                new ServiceResponse(true, "Question Type updated.")
                        .addData("questionType", questionTypeService.update(questionType))
        );
    }

    @DeleteMapping(value = "/{questionTypeId}")
    public ResponseEntity<?> delete(@PathVariable Long questionTypeId) {
        questionTypeService.delete(questionTypeId);
        return ResponseEntity.ok(
                new ServiceResponse(true, "Question Type deleted permanently.")
        );
    }
}
