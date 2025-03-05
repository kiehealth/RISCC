package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityResDto {
    private Long id;
    private String name;
    private StateResDto state;

    public CityResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public CityResDto setName(String name) {
        this.name = name;
        return this;
    }

    public CityResDto setState(StateResDto state) {
        this.state = state;
        return this;
    }
}
