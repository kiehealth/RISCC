package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppVersionDto {
    private Long id;
    private String family;
    private String version;
    private boolean forceUpdate;
    private String url;

    public AppVersionDto setId(Long id) {
        this.id = id;
        return this;
    }

    public AppVersionDto setFamily(String family) {
        this.family = family;
        return this;
    }

    public AppVersionDto setVersion(String version) {
        this.version = version;
        return this;
    }

    public AppVersionDto setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        return this;
    }

    public AppVersionDto setUrl(String url) {
        this.url = url;
        return this;
    }
}
