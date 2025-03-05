package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.SettingDto;
import com.chronelab.riscc.dto.WelcomeDto;
import com.chronelab.riscc.service.SettingService;
import com.chronelab.riscc.service.WelcomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/setting")
@Api(tags = "Setting", description = "APIs related to Setting.")
public class SettingController {

    private final SettingService settingService;
    private final WelcomeService welcomeService;

    @Autowired
    public SettingController(SettingService settingService, WelcomeService welcomeService) {
        this.settingService = settingService;
        this.welcomeService = welcomeService;
    }

    @GetMapping
    @ApiOperation(value = "Get list of all Setting.")
    public ResponseEntity<ServiceResponse> get() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Settings retrieved.")
                        .addData("settings", settingService.get())
        );
    }

    @PutMapping
    @ApiOperation(value = "Update value of an existing Setting.")
    public ResponseEntity<ServiceResponse> update(@RequestBody SettingDto setting) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Setting updated.")
                        .addData("setting", settingService.update(setting))
        );
    }

    @GetMapping(value = "/welcome")
    @ApiOperation(value = "Get Welcome detail.")
    public ResponseEntity<ServiceResponse> getWelcome() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Welcome detail retrieved.")
                        .addData("welcome", welcomeService.get())
        );
    }

    @PutMapping(value = "/welcome")
    @ApiOperation(value = "Update Welcome detail.")
    public ResponseEntity<ServiceResponse> update(@RequestBody WelcomeDto welcome) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Welcome updated.")
                        .addData("welcome", welcomeService.update(welcome))
        );
    }
}
