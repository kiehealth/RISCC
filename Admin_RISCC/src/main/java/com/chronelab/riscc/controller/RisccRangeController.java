package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.RisccRangeReq;
import com.chronelab.riscc.dto.response.RisccRangeRes;
import com.chronelab.riscc.service.RisccRangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/setting/riscc_range")
@Api(tags = "RisccRange", description = "APIS related to RisccRange.")
public class RisccRangeController {

    private final RisccRangeService risccRangeService;
    private final ServiceResponseUtil<RisccRangeRes> risccRangeResServiceResponseUtil;

    @Autowired
    public RisccRangeController(RisccRangeService risccRangeService, ServiceResponseUtil<RisccRangeRes> risccRangeResServiceResponseUtil) {
        this.risccRangeService = risccRangeService;
        this.risccRangeResServiceResponseUtil = risccRangeResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Riscc Range Value.")
    public ResponseEntity<ServiceResponse> save(@RequestBody RisccRangeReq risccRange) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Riscc Range saved.")
                        .addData("risccRange", risccRangeService.save(risccRange))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all RisccRange list. If paginated detail is not provided then all the RisccRange will be featched.")
    public ResponseEntity<ServiceResponse> getPaginated() {
        return ResponseEntity.ok(new ServiceResponse()
                .setStatus(true)
                .setMessage("Riscc Range retrieved.")
                .addData("risccRange", risccRangeService.get())
        );
    }

    @PutMapping
    @ApiOperation(value = "Updating an existing RisccRange.")
    public ResponseEntity<ServiceResponse> update(@RequestBody RisccRangeReq risccRange) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Riscc Range updated.")
                        .addData("risccRange", risccRangeService.update(risccRange))
        );
    }

    @DeleteMapping(value = "/{risccRangeId}")
    @ApiOperation(value = "Delete an existing Riscc Range.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long risccRangeId) {
        risccRangeService.delete(risccRangeId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Riscc Range deleted.")
        );
    }
}
