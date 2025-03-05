package com.chronelab.riscc.dto.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorityResDto {
    private Long id;
    private String title;

    public AuthorityResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public AuthorityResDto setTitle(String title) {
        this.title = title;
        return this;
    }
}
