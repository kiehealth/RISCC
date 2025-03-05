package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class AppAnalyticsKeyPairReq {
    private Long id;

    private Long appAnalyticsKeyOneId;
    private Long appAnalyticsKeyTwoId;
}
