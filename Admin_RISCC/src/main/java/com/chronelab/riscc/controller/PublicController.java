package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.*;
import com.chronelab.riscc.dto.request.AppAnalyticsReq;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.AppAnalyticsKeyRes;
import com.chronelab.riscc.service.AnonService;
import com.chronelab.riscc.service.AnswerService;
import com.chronelab.riscc.service.AppAnalyticsKeyService;
import com.chronelab.riscc.service.ResourceFileService;
import com.chronelab.riscc.service.general.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api/public")
@Api(tags = "Public", description = "APIs that can be accessed without authentication.")
public class PublicController {

    private final ServiceResponseUtil<AppAnalyticsKeyRes> appAnalyticsKeyResDtoServiceResponseUtil;
    private final AnonService anonService;
    private final UserService userService;
    private final AppAnalyticsKeyService appAnalyticsKeyService;
    private final ResourceFileService resourceFileService;
    private final AnswerService answerService;

    @Autowired
    public PublicController(ServiceResponseUtil<AppAnalyticsKeyRes> appAnalyticsKeyResDtoServiceResponseUtil, AnonService anonService, UserService userService, AppAnalyticsKeyService appAnalyticsKeyService, ResourceFileService resourceFileService, AnswerService answerService) {
        this.appAnalyticsKeyResDtoServiceResponseUtil = appAnalyticsKeyResDtoServiceResponseUtil;
        this.anonService = anonService;
        this.userService = userService;
        this.appAnalyticsKeyService = appAnalyticsKeyService;
        this.resourceFileService = resourceFileService;
        this.answerService = answerService;
    }

    @PostMapping(value = "/sign_up/verification_code")
    @ApiOperation(value = "Get verification code for sign up.")
    public ResponseEntity<ServiceResponse> getSignUpVerificationCode(@RequestBody VerificationDto verification) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Verification code sent successfully.")
                        .addData("verification", anonService.sendVerificationCode(verification))
        );
    }

    @PostMapping(value = "/sign_up") //handle post request
    @ApiOperation(value = "Sign Up a new User.") //documenting API operation
    public ResponseEntity<ServiceResponse> signUp(@RequestBody UserReqDto user) throws IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Signed up successfully.")
                        .addData("user", userService.save(user))
        );
    }

    @GetMapping(value = "/password_reset/verification_code")
    @ApiOperation(value = "Get verification code in email or contact number to reset password.")
    public ResponseEntity<ServiceResponse> getVerificationCode(
            @ApiParam(name = "identity", value = "Either Email address or contact number with country code prefixed with +")
            @RequestParam("identity") String identity) {
        String verificationCode = anonService.sendVerificationCode(identity);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage(identity.contains("@") ? "Verification code sent successfully to your email address." : "Verification code sent successfully to your mobile number.")
                        .addData("verificationCode", verificationCode)
        );
    }

    @PutMapping(value = "/reset_password")
    @ApiOperation(value = "Reset password.")
    public ResponseEntity<ServiceResponse> resetPassword(@RequestBody PasswordReqDto resetPassword) {
        anonService.resetPassword(resetPassword.getVerificationCode(), resetPassword.getNewPassword());
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Password reset successful.")
        );
    }

    @PostMapping(value = "/app_analytics")
    @ApiOperation(value = "Save App Analytics.")
    public ResponseEntity<ServiceResponse> saveAppAnalytics(@RequestBody AppAnalyticsReq appAnalytics) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("App Analytics saved successfully.")
                        .addData("appAnalytics", anonService.saveAppAnalytics(appAnalytics))
        );
    }

    @GetMapping(value = "/app_analytics_key")
    @ApiOperation(value = "Get paginated or all App Analytics Key list. If pagination detail is not provided then all data will be fetched.")
    public ResponseEntity<ServiceResponse> getAppAnalyticsKey(PaginationReqDto pagination) {
        return ResponseEntity.ok(appAnalyticsKeyResDtoServiceResponseUtil.buildServiceResponse(
                true,
                "App Analytics Keys retrieved retrieved",
                appAnalyticsKeyService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/app_analytics_key/filter")
    @ApiOperation(value = "Get paginated or all App Analytics Key list by filter. If pagination detail is not provided then all data will be fetched.")
    public ResponseEntity<ServiceResponse> getAppAnalyticsKey(PaginationReqDto pagination, @RequestParam(required = false) ArrayList<String> titlesLike) {
        String title1 = null, title2 = null;
        if (titlesLike != null && titlesLike.size() > 0) {
            title1 = titlesLike.get(0);
        }
        if (titlesLike != null && titlesLike.size() > 1) {
            title2 = titlesLike.get(1);
        }

        return ResponseEntity.ok(appAnalyticsKeyResDtoServiceResponseUtil.buildServiceResponse(
                true,
                "App Analytics Keys retrieved",
                appAnalyticsKeyService.getByFilter(title1, title2, pagination)
                )
        );
    }

    @GetMapping(value = "/resource_file")
    @ApiOperation(value = "Get list of Resource File.")
    public ResponseEntity<ServiceResponse> getResourceFile() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Resource File retrieved.")
                        .addData("resourceFiles", resourceFileService.get())
        );
    }
    @GetMapping(value = "/calculate_riscc")
    @ApiOperation(value = "Calculate the riscc value.")
    public ResponseEntity<ServiceResponse> calculateRisccRange( @RequestParam(required = false) ArrayList<Long> userIds,
                                                                @RequestParam(required = false) ArrayList<Long> questionnaireIds){
        return ResponseEntity.ok(
                new ServiceResponse()
                .setStatus(true)
                 .setMessage("Calculated Riscc value retrieved.")
                .addData("risccValue",answerService.calculateRiscc(userIds,questionnaireIds))
        );
    }

}
