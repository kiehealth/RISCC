package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.FeedbackReq;
import com.chronelab.riscc.dto.response.FeedbackRes;
import com.chronelab.riscc.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/feedback")
@Api(tags = "Feedback", description = "APIs related to Feedback.")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ServiceResponseUtil<FeedbackRes> feedbackResServiceResponseUtil;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, ServiceResponseUtil<FeedbackRes> feedbackResServiceResponseUtil) {
        this.feedbackService = feedbackService;
        this.feedbackResServiceResponseUtil = feedbackResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Feedback.")
    public ResponseEntity<ServiceResponse> save(@RequestBody FeedbackReq feedback) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Feedback saved.")
                        .addData("feedback", feedbackService.save(feedback))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Feedback list. If pagination detail is not provided then all the Feedback will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(feedbackResServiceResponseUtil.buildServiceResponse(
                true,
                "Feedback retrieved",
                feedbackService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/by_user")
    @ApiOperation(value = "Get paginated list or all Feedback list of logged in user. If pagination detail is not provided then all the Feedback of logged in user will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginatedByUser(PaginationReqDto pagination) {

        return ResponseEntity.ok(feedbackResServiceResponseUtil.buildServiceResponse(
                true,
                "Feedback retrieved",
                feedbackService.getByUser(pagination)
                )
        );
    }
}
