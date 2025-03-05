package com.chronelab.riscc.dto;

import lombok.Data;

@Data
public class SettingDto {

    private Long id;
    private String keyTitle;
    private String keyValue;

    public SettingDto setId(Long id) {
        this.id = id;
        return this;
    }

    public SettingDto setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
        return this;
    }

    public SettingDto setKeyValue(String keyValue) {
        this.keyValue = keyValue;
        return this;
    }
}
