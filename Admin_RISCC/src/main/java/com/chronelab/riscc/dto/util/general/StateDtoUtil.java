package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.StateReqDto;
import com.chronelab.riscc.dto.response.general.StateResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateDtoUtil implements DtoUtil<StateEntity, StateReqDto, StateResDto> {

    private final CountryDtoUtil countryDtoUtil;

    @Autowired
    public StateDtoUtil(CountryDtoUtil countryDtoUtil) {
        this.countryDtoUtil = countryDtoUtil;
    }

    @Override
    public StateEntity reqToEntity(StateReqDto stateReqDto) {
        return new StateEntity()
                .setName(stateReqDto.getName());
    }

    @Override
    public StateResDto entityToRes(StateEntity stateEntity) {
        return new StateResDto()
                .setId(stateEntity.getId())
                .setName(stateEntity.getName());
    }

    @Override
    public StateResDto prepRes(StateEntity stateEntity) {
        return entityToRes(stateEntity)
                .setCountry(countryDtoUtil.entityToRes(stateEntity.getCountry()));
    }

    @Override
    public void setUpdatedValue(StateReqDto stateReqDto, StateEntity stateEntity) {
        if (stateReqDto != null && stateEntity != null) {
            if (stateReqDto.getName() != null && !stateReqDto.getName().equals(stateEntity.getName())) {
                stateEntity.setName(stateReqDto.getName());
            }
        }
    }
}
