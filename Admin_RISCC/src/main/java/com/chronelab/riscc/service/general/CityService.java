package com.chronelab.riscc.service.general;


import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.CityReqDto;
import com.chronelab.riscc.dto.response.general.CityResDto;

public interface CityService {
    CityResDto save(CityReqDto cityReqDto);

    PaginationResDto<CityResDto> get(PaginationReqDto paginationReqDto);

    PaginationResDto<CityResDto> getByState(Long stateId, PaginationReqDto paginationReqDto);

    CityResDto update(CityReqDto cityReqDto);

    void delete(Long id);
}
