package com.chronelab.riscc.dto.request.general;

import lombok.Data;

import java.util.List;

@Data
public class RoleReqDto {
    private Long id;
    private String title;
    private List<Long> authorityIds;
}
