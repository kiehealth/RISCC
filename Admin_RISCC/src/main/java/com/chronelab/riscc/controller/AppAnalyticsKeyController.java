package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.AppAnalyticsKeyReq;
import com.chronelab.riscc.service.AppAnalyticsKeyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/app_analytics_key")
@Api(tags = "App Analytics Key")
public class AppAnalyticsKeyController {

    private final AppAnalyticsKeyService appAnalyticsKeyService;

    @Autowired
    public AppAnalyticsKeyController(AppAnalyticsKeyService appAnalyticsKeyService) {
        this.appAnalyticsKeyService = appAnalyticsKeyService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new App Analytics Key.")
    public ResponseEntity<ServiceResponse> save(@RequestBody AppAnalyticsKeyReq appAnalyticsKey) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key saved.")
                        .addData("appAnalyticsKey", appAnalyticsKeyService.save(appAnalyticsKey))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing App Analytics Key.")
    public ResponseEntity<ServiceResponse> update(@RequestBody AppAnalyticsKeyReq appAnalyticsKey) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key updated.")
                        .addData("appAnalyticsKey", appAnalyticsKeyService.update(appAnalyticsKey))
        );
    }

    @DeleteMapping(value = "/{appAnalyticsKeyId}")
    @ApiOperation(value = "Delete an existing App Analytics Key.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long appAnalyticsKeyId) {
        appAnalyticsKeyService.delete(appAnalyticsKeyId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key deleted.")
        );
    }
}
