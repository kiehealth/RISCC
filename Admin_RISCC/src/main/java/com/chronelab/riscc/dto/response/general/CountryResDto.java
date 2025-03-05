package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryResDto {
    private Long id;
    private String name;

    public CountryResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public CountryResDto setName(String name) {
        this.name = name;
        return this;
    }
}
