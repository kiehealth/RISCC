package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.RoleReqDto;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;
import com.chronelab.riscc.dto.response.general.RoleResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDtoUtil implements DtoUtil<RoleEntity, RoleReqDto, RoleResDto> {

    private final AuthorityDtoUtil authorityDtoUtil;

    @Autowired
    public RoleDtoUtil(AuthorityDtoUtil authorityDtoUtil) {
        this.authorityDtoUtil = authorityDtoUtil;
    }

    @Override
    public RoleEntity reqToEntity(RoleReqDto roleReqDto) {
        return new RoleEntity()
                .setTitle(roleReqDto.getTitle());
    }

    @Override
    public RoleResDto entityToRes(RoleEntity roleEntity) {
        return new RoleResDto()
                .setId(roleEntity.getId())
                .setTitle(roleEntity.getTitle());
    }

    @Override
    public RoleResDto prepRes(RoleEntity roleEntity) {
        RoleResDto roleResDto = entityToRes(roleEntity);
        if (roleEntity.getAuthorities() != null && roleEntity.getAuthorities().size() > 0) {
            List<AuthorityResDto> authorityResDtos = new ArrayList<>();
            for (AuthorityEntity authorityEntity : roleEntity.getAuthorities()) {
                authorityResDtos.add(authorityDtoUtil.entityToRes(authorityEntity));
            }
            roleResDto.setAuthorities(authorityResDtos);
        }
        return roleResDto;
    }

    @Override
    public void setUpdatedValue(RoleReqDto roleReqDto, RoleEntity roleEntity) {
        if (roleReqDto != null && roleEntity != null) {
            if (roleReqDto.getTitle() != null && !roleReqDto.getTitle().equals(roleEntity.getTitle())) {
                roleEntity.setTitle(roleReqDto.getTitle());
            }
        }
    }
}
