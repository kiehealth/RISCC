package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.NotificationDto;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.NotificationReq;
import com.chronelab.riscc.dto.response.NotificationRes;
import com.chronelab.riscc.service.NotificationService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/notification")
@Api(tags = "Notification", description = "APIs related to Notification.")
public class NotificationController {

    private final NotificationService notificationService;
    private final ServiceResponseUtil<NotificationRes> notificationResServiceResponseUtil;

    @Autowired
    public NotificationController(NotificationService notificationService, ServiceResponseUtil<NotificationRes> notificationResServiceResponseUtil) {
        this.notificationService = notificationService;
        this.notificationResServiceResponseUtil = notificationResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save and send push notification.")
    public ResponseEntity<ServiceResponse> save(@RequestBody NotificationReq notification) throws FirebaseMessagingException, IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Notification sent.")
                        .addData("note", notificationService.save(notification))
        );
    }

    @PostMapping(value = "/send")
    @ApiOperation(value = "Send push notification to users.")
    public ResponseEntity<ServiceResponse> sendNotification(@RequestBody NotificationDto notification) throws FirebaseMessagingException, IOException {
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Notification sent.")
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Notification list of logged in user. If pagination detail is not provided then all the Notification of logged in user will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(notificationResServiceResponseUtil.buildServiceResponse(
                true,
                "Notifications retrieved",
                notificationService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/broadcasted")
    @ApiOperation(value = "Get paginated list or all Broadcasted Notification list. If pagination detail is not provided then all the Notification will be fetched..")
    public ResponseEntity<ServiceResponse> getBroadcasted(PaginationReqDto pagination) {

        return ResponseEntity.ok(notificationResServiceResponseUtil.buildServiceResponse(
                true,
                "Notifications retrieved",
                notificationService.getBroadcasted(pagination)
                )
        );
    }

    @GetMapping(value = "/by_role/{roleId}")
    @ApiOperation(value = "Get paginated list or all Notification list by Role. If pagination detail is not provided then all the Notification will be fetched..")
    public ResponseEntity<ServiceResponse> getByRole(@PathVariable Long roleId, PaginationReqDto pagination) {

        return ResponseEntity.ok(notificationResServiceResponseUtil.buildServiceResponse(
                true,
                "Notifications retrieved",
                notificationService.getByRole(roleId, pagination)
                )
        );
    }

    @DeleteMapping(value = "/{notificationId}")
    @ApiOperation(value = "Delete an existing Notification.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Notification deleted.")
        );
    }
}
