package com.chronelab.riscc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkRes {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String emailAddress;
    private String contactNumber;

    public LinkRes setId(Long id) {
        this.id = id;
        return this;
    }

    public LinkRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public LinkRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public LinkRes setUrl(String url) {
        this.url = url;
        return this;
    }

    public LinkRes setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public LinkRes setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }
}
