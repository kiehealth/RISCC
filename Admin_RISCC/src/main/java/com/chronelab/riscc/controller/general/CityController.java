package com.chronelab.riscc.controller.general;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.request.general.CityReqDto;
import com.chronelab.riscc.service.general.CityService;
import com.chronelab.riscc.service.impl.general.CityServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/city")
@Api(tags = "City", description = "APIs related to City.")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityServiceImpl cityServiceImpl) {
        this.cityService = cityServiceImpl;
    }

    @PostMapping
    @ApiOperation(value = "Save a new City.")
    public ResponseEntity<ServiceResponse> save(@RequestBody CityReqDto city) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("City saved.")
                        .addData("city", cityService.save(city))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing City.")
    public ResponseEntity<ServiceResponse> update(@RequestBody CityReqDto city) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("City updated.")
                        .addData("city", cityService.update(city))
        );
    }

    @DeleteMapping(value = "/{cityId}")
    @ApiOperation(value = "Delete an existing City.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long cityId) {
        cityService.delete(cityId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("City deleted.")
        );
    }
}
