package com.chronelab.riscc.dto;

import org.springframework.stereotype.Component;

@Component
public class ServiceResponseUtil<Res> {
    public ServiceResponse buildServiceResponse(boolean status, String message, PaginationResDto<Res> paginationResDto) {
        return new ServiceResponse()
                .setStatus(status)
                .setMessage(message)
                .setStartPosition(paginationResDto.getStartPosition())
                .setEndPosition(paginationResDto.getEndPosition())
                .setTotalRecord(paginationResDto.getTotalRecord())
                .setTotalPage(paginationResDto.getTotalPage())
                .setPageSize(paginationResDto.getPageSize())
                .setCurrentPage(paginationResDto.getCurrentPage())
                .addData("list", paginationResDto.getData());
    }
}
