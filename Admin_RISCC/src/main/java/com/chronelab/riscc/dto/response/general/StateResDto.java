package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateResDto {
    private Long id;
    private String name;
    private CountryResDto country;

    public StateResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public StateResDto setName(String name) {
        this.name = name;
        return this;
    }

    public StateResDto setCountry(CountryResDto country) {
        this.country = country;
        return this;
    }
}
