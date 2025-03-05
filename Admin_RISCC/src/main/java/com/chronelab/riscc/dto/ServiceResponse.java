package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {
    private boolean status;
    private String message;

    private Integer startPosition;
    private Integer endPosition;
    private Long totalRecord;
    private Integer totalPage;
    private Integer pageSize;
    private Integer currentPage;

    private List<Object> response;
    private Map<String, Object> data;

    public ServiceResponse() {
    }

    public ServiceResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ServiceResponse setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public ServiceResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceResponse setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public ServiceResponse setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
        return this;
    }

    public ServiceResponse setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
        return this;
    }

    public ServiceResponse setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public ServiceResponse setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ServiceResponse setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public ServiceResponse setResponse(List<Object> response) {
        this.response = response;
        return this;
    }

    public ServiceResponse addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}
