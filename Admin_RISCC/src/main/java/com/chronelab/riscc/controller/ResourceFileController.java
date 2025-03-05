package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.ResourceFileReq;
import com.chronelab.riscc.service.ResourceFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/resource_file")
@Api(tags = "Resource File")
public class ResourceFileController {

    private final ResourceFileService resourceFileService;

    @Autowired
    public ResourceFileController(ResourceFileService resourceFileService) {
        this.resourceFileService = resourceFileService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Resource File.")
    public ResponseEntity<ServiceResponse> save(@ModelAttribute ResourceFileReq resourceFile) throws IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Resource File saved.")
                        .addData("resourceFile", resourceFileService.save(resourceFile))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Resource File.")
    public ResponseEntity<ServiceResponse> update(@ModelAttribute ResourceFileReq resourceFile) throws IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Resource File updated.")
                        .addData("resourceFile", resourceFileService.update(resourceFile))
        );
    }

    @DeleteMapping(value = "/{resourceFileId}")
    @ApiOperation(value = "Delete an existing Resource File.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long resourceFileId) {
        resourceFileService.delete(resourceFileId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Resource File deleted.")
        );
    }
}
