package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResDto {
    private String street;
    private CityResDto city;

    public AddressResDto setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressResDto setCity(CityResDto city) {
        this.city = city;
        return this;
    }
}
