package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class LinkReq {
    private Long id;

    private String title;
    private String description;
    private String url;
    private String emailAddress;
    private String contactNumber;
}
