package com.chronelab.riscc.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotificationDto {
    private String title;
    private String description;
    private List<Long> userIds;

    private Map<String, String> customFields;
}
