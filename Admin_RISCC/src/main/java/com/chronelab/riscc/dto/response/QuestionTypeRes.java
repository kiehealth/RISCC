package com.chronelab.riscc.dto.response;

import lombok.Data;

@Data
public class QuestionTypeRes {
    private Long id;
    private String title;

    public QuestionTypeRes setId(Long id) {
        this.id = id;
        return this;
    }

    public QuestionTypeRes setTitle(String title) {
        this.title = title;
        return this;
    }
}
