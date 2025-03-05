package com.chronelab.riscc.service.general;


import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;

public interface AuthorityService {
    PaginationResDto<AuthorityResDto> get(PaginationReqDto paginationReqDto);
}
