package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.dto.response.general.UserResDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppAnalyticsRes {
    private Long id;
    private String description;
    private String value;

    private AppAnalyticsKeyRes appAnalyticsKey;
    private UserResDto user;

    public AppAnalyticsRes setId(Long id) {
        this.id = id;
        return this;
    }

    public AppAnalyticsRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public AppAnalyticsRes setValue(String value) {
        this.value = value;
        return this;
    }

    public AppAnalyticsRes setAppAnalyticsKey(AppAnalyticsKeyRes appAnalyticsKey) {
        this.appAnalyticsKey = appAnalyticsKey;
        return this;
    }

    public AppAnalyticsRes setUser(UserResDto user) {
        this.user = user;
        return this;
    }
}
