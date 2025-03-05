package com.chronelab.riscc.controller.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.general.RoleReqDto;
import com.chronelab.riscc.dto.response.general.RoleResDto;
import com.chronelab.riscc.service.general.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/role")
@Api(tags = "Role", description = "APIs related to Role.")
public class RoleController {

    private final RoleService roleService;
    private final ServiceResponseUtil<RoleResDto> serviceResponseUtil;

    @Autowired
    public RoleController(RoleService roleService, ServiceResponseUtil<RoleResDto> serviceResponseUtil) {
        this.roleService = roleService;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Role.")
    public ResponseEntity<ServiceResponse> save(@RequestBody RoleReqDto role) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Role saved.")
                        .addData("role", roleService.save(role))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Role list. If pagination detail is not provided then all the roles will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Roles retrieved.",
                roleService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Role.")
    public ResponseEntity<ServiceResponse> update(@RequestBody RoleReqDto role) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Role updated.")
                        .addData("role", roleService.update(role))
        );
    }

    @DeleteMapping(value = "/{roleId}")
    @ApiOperation(value = "Delete an existing Role.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Role deleted.")
        );
    }
}
