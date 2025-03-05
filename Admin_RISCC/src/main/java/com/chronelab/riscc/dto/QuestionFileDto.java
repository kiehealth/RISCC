package com.chronelab.riscc.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionFileDto {
    private String questionType;
    private String questionnaire;
    private String title;
    private String body;
    private List<String> options;
}
