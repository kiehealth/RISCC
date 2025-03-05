package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.AuthorityReqDto;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.enums.Authority;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDtoUtil implements DtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto> {
    @Override
    public AuthorityEntity reqToEntity(AuthorityReqDto authorityReqDto) {
        return new AuthorityEntity()
                .setTitle(authorityReqDto.getTitle());
    }

    @Override
    public AuthorityResDto entityToRes(AuthorityEntity authorityEntity) {
        return new AuthorityResDto()
                .setId(authorityEntity.getId())
                //.setTitle(authorityEntity.getTitle());
                .setTitle(Authority.valueOf(authorityEntity.getTitle()).getDisplayTitle());
    }

    @Override
    public AuthorityResDto prepRes(AuthorityEntity authorityEntity) {
        return entityToRes(authorityEntity);
    }

    @Override
    public void setUpdatedValue(AuthorityReqDto authorityReqDto, AuthorityEntity authorityEntity) {
        if (authorityReqDto != null && authorityEntity != null) {
            if (authorityReqDto.getTitle() != null && !authorityReqDto.getTitle().equals(authorityEntity.getTitle())) {
                authorityEntity.setTitle(authorityReqDto.getTitle());
            }
        }
    }
}
