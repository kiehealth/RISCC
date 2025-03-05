package com.chronelab.riscc.service.general;


import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.CountryReqDto;
import com.chronelab.riscc.dto.response.general.CountryResDto;

public interface CountryService {
    CountryResDto save(CountryReqDto countryReqDto);

    PaginationResDto<CountryResDto> get(PaginationReqDto paginationReqDto);

    CountryResDto update(CountryReqDto countryReqDto);

    void delete(Long id);
}
