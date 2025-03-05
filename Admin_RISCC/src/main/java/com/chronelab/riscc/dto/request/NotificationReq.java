package com.chronelab.riscc.dto.request;

import com.chronelab.riscc.enums.NotificationType;
import lombok.Data;

import java.util.List;

@Data
public class NotificationReq {
    private Long id;
    private String title;
    private String description;
    private NotificationType notificationType;

    private List<Long> userIds;


    private List<Long> roleIds;
    private List<Long> groupIds;
}
