package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.AppAnalyticsKeyPairReq;
import com.chronelab.riscc.service.AppAnalyticsKeyPairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/app_analytics_key_pair")
@Api(tags = "App Analytics Key Pair")
public class AppAnalyticsKeyPairController {

    private final AppAnalyticsKeyPairService appAnalyticsKeyPairService;

    @Autowired
    public AppAnalyticsKeyPairController(AppAnalyticsKeyPairService appAnalyticsKeyPairService) {
        this.appAnalyticsKeyPairService = appAnalyticsKeyPairService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new App Analytics Key Pair.")
    public ResponseEntity<ServiceResponse> save(@RequestBody AppAnalyticsKeyPairReq appAnalyticsKeyPair) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key Pair saved.")
                        .addData("appAnalyticsKeyPair", appAnalyticsKeyPairService.save(appAnalyticsKeyPair))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get list of App Analytics Key Pair.")
    public ResponseEntity<ServiceResponse> getAll() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key Pair retrieved.")
                        .addData("appAnalyticsKeyPairs", appAnalyticsKeyPairService.getAll())
        );
    }

    @GetMapping("/type_two_not_null")
    @ApiOperation(value = "Get list of App Analytics Key Pair that has both types.")
    public ResponseEntity<ServiceResponse> getByTypeTwoNotNull() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key Pair retrieved.")
                        .addData("appAnalyticsKeyPairs", appAnalyticsKeyPairService.getByKeyTwoNotNull())
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing App Analytics Key Pair.")
    public ResponseEntity<ServiceResponse> update(@RequestBody AppAnalyticsKeyPairReq appAnalyticsKeyPair) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key Pair updated.")
                        .addData("appAnalyticsKeyPair", appAnalyticsKeyPairService.update(appAnalyticsKeyPair))
        );
    }

    @DeleteMapping(value = "/{appAnalyticsKeyPairId}")
    @ApiOperation(value = "Delete an existing App Analytics Key Pair.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long appAnalyticsKeyPairId) {
        appAnalyticsKeyPairService.delete(appAnalyticsKeyPairId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics Key Pair deleted.")
        );
    }
}
