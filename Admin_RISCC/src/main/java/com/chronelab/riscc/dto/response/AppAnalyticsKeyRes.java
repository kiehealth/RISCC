package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.enums.AppAnalyticsKeyDataType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppAnalyticsKeyRes {
    private Long id;
    private String title;
    private String description;
    private AppAnalyticsKeyDataType appAnalyticsKeyDataType;

    public AppAnalyticsKeyRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AppAnalyticsKeyRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public AppAnalyticsKeyRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public AppAnalyticsKeyRes setAppAnalyticsKeyDataType(AppAnalyticsKeyDataType appAnalyticsKeyDataType) {
        this.appAnalyticsKeyDataType = appAnalyticsKeyDataType;
        return this;
    }
}
