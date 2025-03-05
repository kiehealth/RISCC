package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.AboutUsDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.service.AboutUsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/about_us")
@Api(tags = "Setting", description = "APIs related to Setting.")
public class AboutUsController {

    private final AboutUsService aboutUsService;

    @Autowired
    public AboutUsController(AboutUsService aboutUsService) {
        this.aboutUsService = aboutUsService;
    }

    @GetMapping
    @ApiOperation(value = "Get About Us.")
    public ResponseEntity<ServiceResponse> get() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("About Us retrieved.")
                        .addData("aboutUs", aboutUsService.get())
        );
    }

    @PutMapping
    @ApiOperation(value = "Update About Us.")
    public ResponseEntity<ServiceResponse> update(@RequestBody AboutUsDto aboutUs) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("About Us updated.")
                        .addData("aboutUs", aboutUsService.update(aboutUs))
        );
    }
}
