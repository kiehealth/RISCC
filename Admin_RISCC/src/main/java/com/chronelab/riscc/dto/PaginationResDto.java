package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResDto<Res> {
    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;
    private List<Res> data;

    public PaginationResDto<Res> setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public PaginationResDto<Res> setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public PaginationResDto<Res> setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public PaginationResDto<Res> setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PaginationResDto<Res> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PaginationResDto<Res> setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PaginationResDto<Res> setData(List<Res> data) {
        this.data = data;
        return this;
    }
}
