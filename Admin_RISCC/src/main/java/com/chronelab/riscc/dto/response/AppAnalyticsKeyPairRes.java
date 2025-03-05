package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppAnalyticsKeyPairRes {
    private Long id;
    private AppAnalyticsKeyRes appAnalyticsKeyOne;
    private AppAnalyticsKeyRes appAnalyticsKeyTwo;

    public AppAnalyticsKeyPairRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AppAnalyticsKeyPairRes setAppAnalyticsKeyOne(AppAnalyticsKeyRes appAnalyticsKeyOne) {
        this.appAnalyticsKeyOne = appAnalyticsKeyOne;
        return this;
    }

    public AppAnalyticsKeyPairRes setAppAnalyticsKeyTwo(AppAnalyticsKeyRes appAnalyticsKeyTwo) {
        this.appAnalyticsKeyTwo = appAnalyticsKeyTwo;
        return this;
    }
}
