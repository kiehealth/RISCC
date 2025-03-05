package com.chronelab.riscc.service.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.RoleReqDto;
import com.chronelab.riscc.dto.response.general.RoleResDto;

public interface RoleService {
    RoleResDto save(RoleReqDto roleReqDto);

    PaginationResDto<RoleResDto> get(PaginationReqDto paginationReqDto);

    RoleResDto update(RoleReqDto roleReqDto);

    void delete(Long id);
}
