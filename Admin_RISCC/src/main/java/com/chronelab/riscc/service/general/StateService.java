package com.chronelab.riscc.service.general;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.StateReqDto;
import com.chronelab.riscc.dto.response.general.StateResDto;

public interface StateService {
    StateResDto save(StateReqDto stateReqDto);

    PaginationResDto<StateResDto> get(PaginationReqDto paginationReqDto);

    PaginationResDto<StateResDto> getByCountry(Long countryId, PaginationReqDto paginationReqDto);

    StateResDto update(StateReqDto stateReqDto);

    void delete(Long id);
}
