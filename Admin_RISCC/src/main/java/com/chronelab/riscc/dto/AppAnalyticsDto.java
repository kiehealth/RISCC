package com.chronelab.riscc.dto;

import com.chronelab.riscc.dto.response.general.UserResDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppAnalyticsDto {
    private String title1;
    private String value1;
    private String title2;
    private String value2;
    private String difference;//hh:mm:ss
    private UserResDto user;
}
