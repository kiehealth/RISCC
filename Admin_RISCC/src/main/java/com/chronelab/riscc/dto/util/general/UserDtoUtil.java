package com.chronelab.riscc.dto.util.general;

import com.chronelab.riscc.dto.request.general.AuthorityReqDto;
import com.chronelab.riscc.dto.request.general.RoleReqDto;
import com.chronelab.riscc.dto.request.general.UserReqDto;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;
import com.chronelab.riscc.dto.response.general.RoleResDto;
import com.chronelab.riscc.dto.response.general.UserResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.entity.general.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoUtil implements DtoUtil<UserEntity, UserReqDto, UserResDto> {

    private final DtoUtil<RoleEntity, RoleReqDto, RoleResDto> roleDtoUtil;
    private final DtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto> authorityDtoUtil;

    @Autowired
    public UserDtoUtil(DtoUtil<RoleEntity, RoleReqDto, RoleResDto> roleDtoUtil, DtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto> authorityDtoUtil) {
        this.roleDtoUtil = roleDtoUtil;
        this.authorityDtoUtil = authorityDtoUtil;
    }

    @Override
    public UserEntity reqToEntity(UserReqDto userReqDto) {
        return new UserEntity()
                .setFirstName(userReqDto.getFirstName())
                .setLastName(userReqDto.getLastName())
                .setGender(userReqDto.getGender())
                .setDateOfBirth(userReqDto.getDateOfBirth())
                .setMobileNumber(userReqDto.getMobileNumber())
                .setEmailAddress(userReqDto.getEmailAddress());
    }

    @Override
    public UserResDto entityToRes(UserEntity userEntity) {
        return new UserResDto()
                .setId(userEntity.getId())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName())
                .setGender(userEntity.getGender())
                .setDateOfBirth(userEntity.getDateOfBirth())
                .setMobileNumber(userEntity.getMobileNumber())
                .setEmailAddress(userEntity.getEmailAddress())
                .setImageUrl(userEntity.getImageUrl())
                .setCreatedDateTime(userEntity.getCreatedDate());
    }

    @Override
    public UserResDto prepRes(UserEntity userEntity) {
        UserResDto userResDto = entityToRes(userEntity);

        //Role and Authority
        RoleResDto roleResDto = roleDtoUtil.entityToRes(userEntity.getRole());
        List<AuthorityResDto> authorityResDtos = new ArrayList<>();
        userEntity.getRole().getAuthorities().forEach(authorityEntity -> authorityResDtos.add(authorityDtoUtil.entityToRes(authorityEntity)));
        roleResDto.setAuthorities(authorityResDtos);

        userResDto.setRole(roleResDto);

        if (userEntity.getAnswers() != null && userEntity.getAnswers().size() > 0) {
            userResDto.setHasAnsweredQuestionnaire(true);
        }

        return userResDto;
    }

    @Override
    public void setUpdatedValue(UserReqDto userReqDto, UserEntity userEntity) {
        if (userReqDto.getFirstName() != null && !userReqDto.getFirstName().equals(userEntity.getFirstName())) {
            userEntity.setFirstName(userReqDto.getFirstName());
        }

        if (userReqDto.getLastName() != null && !userReqDto.getLastName().equals(userEntity.getLastName())) {
            userEntity.setLastName(userReqDto.getLastName());
        }

        if (userReqDto.getGender() != null &&
                (userEntity.getGender() == null || !userReqDto.getGender().equals(userEntity.getGender()))) {
            userEntity.setGender(userReqDto.getGender());
        }

        if (userReqDto.getDateOfBirth() != null &&
                (userEntity.getDateOfBirth() == null || !userReqDto.getDateOfBirth().equals(userEntity.getDateOfBirth()))) {
            userEntity.setDateOfBirth(userReqDto.getDateOfBirth());
        }

        if (userReqDto.getMobileNumber() != null &&
                (userEntity.getMobileNumber() == null || !userReqDto.getMobileNumber().equals(userEntity.getMobileNumber()))) {
            userEntity.setMobileNumber(userReqDto.getMobileNumber());
        }
        if (userReqDto.getEmailAddress() != null && !userReqDto.getEmailAddress().equals(userEntity.getEmailAddress())) {
            userEntity.setEmailAddress(userReqDto.getEmailAddress());
        }
    }
}
