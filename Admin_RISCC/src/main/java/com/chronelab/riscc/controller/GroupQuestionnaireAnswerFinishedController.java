package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.GroupQuestionnaireAnswerFinishedReq;
import com.chronelab.riscc.service.GroupQuestionnaireAnswerFinishedService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/group_questionnaire_answer_finished")
public class GroupQuestionnaireAnswerFinishedController {

    private final GroupQuestionnaireAnswerFinishedService groupQuestionnaireAnswerFinishedService;

    @Autowired
    public GroupQuestionnaireAnswerFinishedController(GroupQuestionnaireAnswerFinishedService groupQuestionnaireAnswerFinishedService) {
        this.groupQuestionnaireAnswerFinishedService = groupQuestionnaireAnswerFinishedService;
    }

    @PostMapping
    @ApiOperation(value = "Save Group Questionnaire Answer Finished.")
    public ResponseEntity<ServiceResponse> save(@RequestBody GroupQuestionnaireAnswerFinishedReq answerFinished) {
        groupQuestionnaireAnswerFinishedService.save(answerFinished);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Answer finished.")
        );
    }
}
