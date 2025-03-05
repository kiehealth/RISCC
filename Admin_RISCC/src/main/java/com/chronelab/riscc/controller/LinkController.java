package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.LinkReq;
import com.chronelab.riscc.dto.response.LinkRes;
import com.chronelab.riscc.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/link")
@Api(tags = "Link", description = "APIs related to Link.")
public class LinkController {

    private final LinkService linkService;
    private final ServiceResponseUtil<LinkRes> linkResServiceResponseUtil;

    @Autowired
    public LinkController(LinkService linkService, ServiceResponseUtil<LinkRes> linkResServiceResponseUtil) {
        this.linkService = linkService;
        this.linkResServiceResponseUtil = linkResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Info.")
    public ResponseEntity<ServiceResponse> save(@RequestBody LinkReq link) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Link saved.")
                        .addData("link", linkService.save(link))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Link list. If pagination detail is not provided then all the Link will be fetched.")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(linkResServiceResponseUtil.buildServiceResponse(
                true,
                "Links retrieved",
                linkService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Info.")
    public ResponseEntity<ServiceResponse> update(@RequestBody LinkReq link) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Link updated.")
                        .addData("link", linkService.update(link))
        );
    }

    @DeleteMapping(value = "/{linkId}")
    @ApiOperation(value = "Delete an existing Info.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long linkId) {
        linkService.delete(linkId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Link deleted.")
        );
    }
}
