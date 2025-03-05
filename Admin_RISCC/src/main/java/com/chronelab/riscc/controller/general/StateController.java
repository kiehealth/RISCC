package com.chronelab.riscc.controller.general;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.general.StateReqDto;
import com.chronelab.riscc.service.general.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/state")
@Api(tags = "State", description = "APIs related to State.")
public class StateController {

    private final StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new State.")
    public ResponseEntity<ServiceResponse> save(@RequestBody StateReqDto state) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("State saved.")
                        .addData("state", stateService.save(state))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing State.")
    public ResponseEntity<ServiceResponse> update(@RequestBody StateReqDto state) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("State updated.")
                        .addData("state", stateService.update(state))
        );
    }

    @DeleteMapping(value = "/{stateId}")
    @ApiOperation(value = "Delete an existing State.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long stateId) {
        stateService.delete(stateId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("State deleted.")
        );
    }
}
