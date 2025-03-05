package com.chronelab.riscc.dto.util.general;


import com.chronelab.riscc.dto.request.general.CityReqDto;
import com.chronelab.riscc.dto.request.general.CountryReqDto;
import com.chronelab.riscc.dto.request.general.StateReqDto;
import com.chronelab.riscc.dto.response.general.AddressResDto;
import com.chronelab.riscc.dto.response.general.CityResDto;
import com.chronelab.riscc.dto.response.general.CountryResDto;
import com.chronelab.riscc.dto.response.general.StateResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AddressEntity;
import com.chronelab.riscc.entity.general.CityEntity;
import com.chronelab.riscc.entity.general.CountryEntity;
import com.chronelab.riscc.entity.general.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoUtil implements DtoUtil<AddressEntity, Object, AddressResDto> {

    private final DtoUtil<CountryEntity, CountryReqDto, CountryResDto> countryDtoUtil;
    private final DtoUtil<StateEntity, StateReqDto, StateResDto> stateDtoUtil;
    private final DtoUtil<CityEntity, CityReqDto, CityResDto> cityDtoUtil;

    @Autowired
    public AddressDtoUtil(DtoUtil<CountryEntity, CountryReqDto, CountryResDto> countryDtoUtil, DtoUtil<StateEntity, StateReqDto, StateResDto> stateDtoUtil, DtoUtil<CityEntity, CityReqDto, CityResDto> cityDtoUtil) {
        this.countryDtoUtil = countryDtoUtil;
        this.stateDtoUtil = stateDtoUtil;
        this.cityDtoUtil = cityDtoUtil;
    }

    @Override
    public AddressEntity reqToEntity(Object o) {
        return null;
    }

    @Override
    public AddressResDto entityToRes(AddressEntity addressEntity) {
        return null;
    }

    @Override
    public AddressResDto prepRes(AddressEntity addressEntity) {
        CountryResDto countryResDto = countryDtoUtil.entityToRes(addressEntity.getCity().getState().getCountry());

        StateResDto stateResDto = stateDtoUtil.entityToRes(addressEntity.getCity().getState());
        stateResDto.setCountry(countryResDto);

        CityResDto cityResDto = cityDtoUtil.entityToRes(addressEntity.getCity());
        cityResDto.setState(stateResDto);

        AddressResDto addressResDto = new AddressResDto();
        addressResDto.setStreet(addressEntity.getStreet());
        addressResDto.setCity(cityResDto);

        return addressResDto;
    }

    @Override
    public void setUpdatedValue(Object o, AddressEntity addressEntity) {

    }
}
