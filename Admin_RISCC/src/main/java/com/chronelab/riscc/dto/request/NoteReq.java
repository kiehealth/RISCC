package com.chronelab.riscc.dto.request;

import lombok.Data;

@Data
public class NoteReq {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
