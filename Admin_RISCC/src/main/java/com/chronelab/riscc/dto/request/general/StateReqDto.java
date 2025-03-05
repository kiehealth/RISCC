package com.chronelab.riscc.dto.request.general;

import lombok.Data;

@Data
public class StateReqDto {
    private Long id;
    private String name;
    private Long countryId;
}
