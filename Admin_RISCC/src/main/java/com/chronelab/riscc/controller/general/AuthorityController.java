package com.chronelab.riscc.controller.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;
import com.chronelab.riscc.service.general.AuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/authority")
@Api(tags = "Authority", description = "APIs related to Authority.")
public class AuthorityController {

    private final AuthorityService authorityService;
    private final ServiceResponseUtil<AuthorityResDto> serviceResponseUtil;

    @Autowired
    public AuthorityController(AuthorityService authorityService, ServiceResponseUtil<AuthorityResDto> serviceResponseUtil) {
        this.authorityService = authorityService;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Authority list. If pagination detail is not provided then all the Authorities will be fetched..")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Authorities retrieved.",
                authorityService.get(pagination)
                )
        );
    }
}
