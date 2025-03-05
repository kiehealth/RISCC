package com.chronelab.riscc.dto.response;

import com.chronelab.riscc.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteRes {
    private Long id;
    private String title;
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDateTime;

    public NoteRes setId(Long id) {
        this.id = id;
        return this;
    }

    public NoteRes setTitle(String title) {
        this.title = title;
        return this;
    }

    public NoteRes setDescription(String description) {
        this.description = description;
        return this;
    }

    public NoteRes setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }
}
