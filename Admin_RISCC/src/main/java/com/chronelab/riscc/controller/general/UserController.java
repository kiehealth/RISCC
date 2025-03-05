package com.chronelab.riscc.controller.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PasswordReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.repo.projection.UserProjection;
import com.chronelab.riscc.service.general.UserService;
import com.chronelab.riscc.service.impl.general.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@Api(tags = "User")
public class UserController {

    private final UserService userService;
    private final ServiceResponseUtil<UserResDto> serviceResponseUtil;
    private final ServiceResponseUtil<UserProjection> userProjectionServiceResponseUtil;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, ServiceResponseUtil<UserResDto> serviceResponseUtil, ServiceResponseUtil<UserProjection> userProjectionServiceResponseUtil) {
        this.userService = userServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.userProjectionServiceResponseUtil = userProjectionServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new User.")
    public ResponseEntity<ServiceResponse> signUp(@RequestBody UserReqDto user) throws IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("User saved.")
                        .addData("user", userService.save(user))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all User list. If pagination detail is not provided then all the users will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Users retrieved.",
                userService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/by_role/{roleId}")
    @ApiOperation(value = "Get paginated list or all User list by Role. If pagination detail is not provided then all the data will be fetched..")
    public ResponseEntity<ServiceResponse> getByRole(PaginationReqDto pagination, @PathVariable Long roleId) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Users retrieved.",
                userService.getByRole(pagination, roleId)
                )
        );
    }

    @GetMapping(value = "/by_role_title")
    @ApiOperation(value = "Get paginated list or all User list by Role Titles. If pagination detail is not provided then all the data will be fetched..")
    public ResponseEntity<ServiceResponse> getByRoleTitles(PaginationReqDto pagination,
                                                           @RequestParam List<String> roleTitles) {
        return ResponseEntity.ok(userProjectionServiceResponseUtil.buildServiceResponse(
                true,
                "Users retrieved.",
                userService.getByRoleTitles(pagination, roleTitles)
                )
        );
    }

    @GetMapping(value = "/profile")
    @ApiOperation(value = "Get logged in User detail.")
    public ResponseEntity<ServiceResponse> getUser() {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("User retrieved.")
                        .addData("user", userService.get())
        );
    }

    @GetMapping(value = "/fields")
    @ApiOperation(value = "Get paginated list or all User list with limited fields. If pagination detail is not provided then all the users will be fetched..")
    public ResponseEntity<ServiceResponse> getLimited(PaginationReqDto pagination,
                                                      @RequestParam(required = false) List<String> fields) {

        return ResponseEntity.ok(userProjectionServiceResponseUtil.buildServiceResponse(
                true,
                "Users retrieved.",
                userService.getLimited(fields, pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing User.")
    public ResponseEntity<ServiceResponse> update(@RequestBody UserReqDto user) throws IOException {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("User updated.")
                        .addData("user", userService.update(user))
        );
    }

    @PutMapping(value = "/change_password")
    @ApiOperation(value = "Change password.")
    public ResponseEntity<ServiceResponse> changePassword(@RequestBody PasswordReqDto password) {
        userService.changePassword(password);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Password changed.")
        );
    }

    @DeleteMapping(value = "/{userId}")
    @ApiOperation(value = "Permanently delete an existing User and his/her related data along with.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("User deleted permanently.")
        );
    }

    @PutMapping(value = "/logout")
    @ApiOperation(value = "Logout User from Device by marking as loggedout")
    public ResponseEntity<ServiceResponse> logout() {
        userService.logout();
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Logged out.")
        );
    }
}
