package com.chronelab.riscc.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResourceFileReq {
    private Long id;
    private String title;
    private String description;
    private String url;

    private MultipartFile resourceFile;
}
