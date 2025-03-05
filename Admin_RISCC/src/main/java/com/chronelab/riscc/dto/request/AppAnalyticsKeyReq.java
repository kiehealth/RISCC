package com.chronelab.riscc.dto.request;

import com.chronelab.riscc.enums.AppAnalyticsKeyDataType;
import lombok.Data;

@Data
public class AppAnalyticsKeyReq {
    private Long id;
    private String title;
    private String description;
    private AppAnalyticsKeyDataType appAnalyticsKeyDataType;
}
