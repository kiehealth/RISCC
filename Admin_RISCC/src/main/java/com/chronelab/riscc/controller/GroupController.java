package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.GroupReq;
import com.chronelab.riscc.dto.response.GroupRes;
import com.chronelab.riscc.service.GroupService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/group")
@Api(tags = "Group")
public class GroupController {

    private static final Logger LOG = LogManager.getLogger();;
    private final GroupService groupService;
    private final ServiceResponseUtil<GroupRes> groupResServiceResponseUtil;

    @Autowired
    public GroupController(GroupService groupService, ServiceResponseUtil<GroupRes> groupResServiceResponseUtil) {
        this.groupService = groupService;
        this.groupResServiceResponseUtil = groupResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Group.")
    public ResponseEntity<ServiceResponse> save(@RequestBody GroupReq group) {
        GroupRes temp = groupService.save(group);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Group saved.")
                        .addData("group", temp)
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Group list. If pagination detail is not provided then all the Group will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(groupResServiceResponseUtil.buildServiceResponse(
                true,
                "Group retrieved",
                groupService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/by_user")
    @ApiOperation(value = "Get Group list of logged in user.")
    public ResponseEntity<ServiceResponse> getByUser() {

        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Group retrieved.")
                        .addData("group", groupService.getByUser())
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Group.")
    public ResponseEntity<ServiceResponse> update(@RequestBody GroupReq group) {

        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Group updated.")
                        .addData("group", groupService.update(group))
        );
    }

    @DeleteMapping(value = "/{groupId}")
    @ApiOperation(value = "Delete group.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Group deleted.")
        );
    }
}
