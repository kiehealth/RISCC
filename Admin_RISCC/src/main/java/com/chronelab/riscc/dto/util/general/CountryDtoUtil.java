package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.CountryReqDto;
import com.chronelab.riscc.dto.response.general.CountryResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.CountryEntity;
import org.springframework.stereotype.Component;

@Component
public class CountryDtoUtil implements DtoUtil<CountryEntity, CountryReqDto, CountryResDto> {
    @Override
    public CountryEntity reqToEntity(CountryReqDto countryReqDto) {
        return new CountryEntity()
                .setName(countryReqDto.getName());
    }

    @Override
    public CountryResDto entityToRes(CountryEntity countryEntity) {
        return new CountryResDto()
                .setId(countryEntity.getId())
                .setName(countryEntity.getName());
    }

    @Override
    public CountryResDto prepRes(CountryEntity countryEntity) {
        return entityToRes(countryEntity);
    }

    @Override
    public void setUpdatedValue(CountryReqDto countryReqDto, CountryEntity countryEntity) {
        if (countryReqDto != null && countryEntity != null) {
            if (countryReqDto.getName() != null && !countryReqDto.getName().equals(countryEntity.getName())) {
                countryEntity.setName(countryReqDto.getName());
            }
        }
    }
}
