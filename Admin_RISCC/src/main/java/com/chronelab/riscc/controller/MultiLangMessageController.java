package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.MultiLangMessageReq;
import com.chronelab.riscc.dto.response.MultiLangMessageRes;
import com.chronelab.riscc.service.MultiLangMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/multi_lang_message")
@Api(tags = "Multi-Language Message")
public class MultiLangMessageController {

    private final MultiLangMessageService multiLangMessageService;
    private final ServiceResponseUtil<MultiLangMessageRes> multiLangMessageResServiceResponseUtil;

    @Autowired
    public MultiLangMessageController(MultiLangMessageService multiLangMessageService, ServiceResponseUtil<MultiLangMessageRes> multiLangMessageResServiceResponseUtil) {
        this.multiLangMessageService = multiLangMessageService;
        this.multiLangMessageResServiceResponseUtil = multiLangMessageResServiceResponseUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all MultiLangMessage list. If pagination detail is not provided then all the MultiLangMessage will be fetched.")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(multiLangMessageResServiceResponseUtil.buildServiceResponse(
                true,
                "MultiLangMessages retrieved",
                multiLangMessageService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Info.")
    public ResponseEntity<ServiceResponse> update(@RequestBody MultiLangMessageReq multiLangMessage) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("MultiLangMessage updated.")
                        .addData("multiLangMessage", multiLangMessageService.update(multiLangMessage))
        );
    }
}
