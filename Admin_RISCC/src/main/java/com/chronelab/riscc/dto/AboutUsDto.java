package com.chronelab.riscc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AboutUsDto {
    private Long id;
    private String name;
    private String phone;
    private String email;

    public AboutUsDto setId(Long id) {
        this.id = id;
        return this;
    }

    public AboutUsDto setName(String name) {
        this.name = name;
        return this;
    }

    public AboutUsDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AboutUsDto setEmail(String email) {
        this.email = email;
        return this;
    }
}
