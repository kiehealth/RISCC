package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.service.GroupQuestionnaireService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/group_questionnaire")
@Api(tags = "Group Questionnaire")
public class GroupQuestionnaireController {

    private final GroupQuestionnaireService groupQuestionnaireService;

    @Autowired
    public GroupQuestionnaireController(GroupQuestionnaireService groupQuestionnaireService) {
        this.groupQuestionnaireService = groupQuestionnaireService;
    }

    @GetMapping(value = "/active")
    @ApiOperation(value = "Get list of active Questionnaires.")
    public ResponseEntity<ServiceResponse> getActive() {

        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Active Questionnaires retrieved.")
                        .addData("groupQuestionnaires", groupQuestionnaireService.getActiveGroupQuestionnaire())
        );
    }

    @GetMapping
    @ApiOperation(value = "Get list of Group Questionnaires of logged in user.")
    public ResponseEntity<ServiceResponse> get() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Group Questionnaires retrieved.")
                        .addData("groupQuestionnaires", groupQuestionnaireService.get())
        );
    }
}
