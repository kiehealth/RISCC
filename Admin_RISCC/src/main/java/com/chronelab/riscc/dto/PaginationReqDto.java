package com.chronelab.riscc.dto;

import lombok.Data;

@Data
public class PaginationReqDto {
    private int pageNumber;
    private int pageSize;
    private String sortOrder;
    private String sortBy;
    private String searchTerm;
}
