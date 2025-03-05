package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResDto {
    private Long id;
    private String title;
    private List<AuthorityResDto> authorities;

    public RoleResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public RoleResDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public RoleResDto setAuthorities(List<AuthorityResDto> authorities) {
        this.authorities = authorities;
        return this;
    }
}
