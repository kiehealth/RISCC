package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class MultiLangMessageReq {
    private Long id;
    private String english;
    private String swedish;
    private String spanish;
}
