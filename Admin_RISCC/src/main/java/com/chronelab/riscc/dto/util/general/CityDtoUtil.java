package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.CityReqDto;
import com.chronelab.riscc.dto.response.general.CityResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityDtoUtil implements DtoUtil<CityEntity, CityReqDto, CityResDto> {

    private final StateDtoUtil stateDtoUtil;

    @Autowired
    public CityDtoUtil(StateDtoUtil stateDtoUtil) {
        this.stateDtoUtil = stateDtoUtil;
    }

    @Override
    public CityEntity reqToEntity(CityReqDto cityReqDto) {
        return new CityEntity()
                .setName(cityReqDto.getName());
    }

    @Override
    public CityResDto entityToRes(CityEntity cityEntity) {
        return new CityResDto()
                .setId(cityEntity.getId())
                .setName(cityEntity.getName());
    }

    @Override
    public CityResDto prepRes(CityEntity cityEntity) {
        return entityToRes(cityEntity)
                .setState(stateDtoUtil.prepRes(cityEntity.getState()));
    }

    @Override
    public void setUpdatedValue(CityReqDto cityReqDto, CityEntity cityEntity) {
        if (cityReqDto != null && cityEntity != null) {
            if (cityReqDto.getName() != null && !cityReqDto.getName().equals(cityEntity.getName())) {
                cityEntity.setName(cityReqDto.getName());
            }
        }
    }
}
