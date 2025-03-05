package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.AppVersionDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.service.AppVersionService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/app_version")
@Api(tags = "Setting", description = "APIs related to Setting.")
public class AppVersionController {

    private final AppVersionService appVersionService;

    @Autowired
    public AppVersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @PutMapping
    @ApiOperation(value = "Update App Version")
    public ResponseEntity<ServiceResponse> update(@RequestBody AppVersionDto appVersionDto) throws FirebaseMessagingException, IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Version updated successfully.")
                        .addData("updatedAppVersion", appVersionService.update(appVersionDto))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get App Versions.")
    public ResponseEntity<ServiceResponse> get() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Version retrieved successfully.")
                        .addData("appVersion", appVersionService.get())
        );
    }
}
