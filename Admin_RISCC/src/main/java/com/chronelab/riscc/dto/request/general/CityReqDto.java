package com.chronelab.riscc.dto.request.general;

import lombok.Data;

@Data
public class CityReqDto {
    private Long id;
    private String name;
    private Long stateId;
}
