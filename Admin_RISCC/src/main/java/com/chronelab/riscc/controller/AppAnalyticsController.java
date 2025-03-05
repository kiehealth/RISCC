package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.AppAnalyticsDto;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.response.AppAnalyticsRes;
import com.chronelab.riscc.service.AppAnalyticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/app_analytics")
@Api(tags = "App Analytics", description = "APIs related to App Analytics.")
public class AppAnalyticsController {

    private final AppAnalyticsService appAnalyticsService;
    private final ServiceResponseUtil<AppAnalyticsRes> appAnalyticsResServiceResponseUtil;
    private final ServiceResponseUtil<AppAnalyticsDto> appAnalyticsDtoServiceResponseUtil;

    @Autowired
    public AppAnalyticsController(AppAnalyticsService appAnalyticsService, ServiceResponseUtil<AppAnalyticsRes> appAnalyticsResServiceResponseUtil, ServiceResponseUtil<AppAnalyticsDto> appAnalyticsDtoServiceResponseUtil) {
        this.appAnalyticsService = appAnalyticsService;
        this.appAnalyticsResServiceResponseUtil = appAnalyticsResServiceResponseUtil;
        this.appAnalyticsDtoServiceResponseUtil = appAnalyticsDtoServiceResponseUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all App Analytics list. If pagination detail is not provided then all the data will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(appAnalyticsResServiceResponseUtil.buildServiceResponse(
                true,
                "App Analytics retrieved",
                appAnalyticsService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/by_user_type/{userId}/{appAnalyticsTypeId}")
    @ApiOperation(value = "Get paginated list or all App Analytics list by user. If pagination detail is not provided then all the data of user will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginatedByUser(@PathVariable Long userId, @PathVariable Long appAnalyticsTypeId, PaginationReqDto pagination) {

        return ResponseEntity.ok(appAnalyticsResServiceResponseUtil.buildServiceResponse(
                true,
                "App Analytics retrieved",
                appAnalyticsService.getByUserAndType(userId, appAnalyticsTypeId, pagination)
                )
        );
    }

    @GetMapping(value = "/filter")
    @ApiOperation(value = "Get all or paginated list of App Analytics by User and App Analytics Type. If pagination detail is not provided then all data will be fetched.")
    public ResponseEntity<ServiceResponse> getByUser(PaginationReqDto pagination,
                                                     @RequestParam(required = false) ArrayList<Long> userIds,
                                                     @RequestParam(required = false) List<Long> appAnalyticsTypePairIds) {

        return ResponseEntity.ok(appAnalyticsDtoServiceResponseUtil.buildServiceResponse(
                true,
                "App Analytics retrieved",
                appAnalyticsService.getWithFilter(pagination, userIds, appAnalyticsTypePairIds)
                )
        );
    }

    @GetMapping(value = "/export/{fileType}")
    @ApiOperation(value = "Export App Analytics data to file.")
    public ResponseEntity<ServiceResponse> export(@RequestHeader HttpHeaders httpHeaders,
                                                  @PathVariable String fileType,
                                                  @RequestParam(required = false) ArrayList<Long> userIds,
                                                  @RequestParam(required = false) List<Long> appAnalyticsTypePairIds,
                                                  HttpServletResponse response) throws IOException {
        String savedFile = appAnalyticsService.exportFile(fileType, Integer.parseInt(httpHeaders.get("timeZoneOffsetInMinute").get(0)), userIds, appAnalyticsTypePairIds);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .addData("fileUrl", savedFile)
        );
    }
}
