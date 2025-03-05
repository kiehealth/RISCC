package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class RisccRangeReq {
    private Long id;
    private String fromValue;
    private String toValue;
    private String message;
    private String moreInfo;
    private Long questionaryId;
}
