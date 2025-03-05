package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceFileRes {
    private Long id;
    private String title;
    private String description;
    private String url;

    public ResourceFileRes setId(Long id) {
        this.id = id;
        return this;
    }

    public ResourceFileRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public ResourceFileRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public ResourceFileRes setUrl(String url) {
        this.url = url;
        return this;
    }
}
