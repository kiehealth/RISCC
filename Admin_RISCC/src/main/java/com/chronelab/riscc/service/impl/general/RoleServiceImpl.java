package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.RoleReqDto;
import com.chronelab.riscc.dto.response.general.RoleResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.entity.general.RoleEntity;
import com.chronelab.riscc.exception.CustomException;
import com.chronelab.riscc.repo.general.AuthorityRepo;
import com.chronelab.riscc.repo.general.RoleRepo;
import com.chronelab.riscc.service.general.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LogManager.getLogger();

    private final RoleRepo roleRepo;
    private final AuthorityRepo authorityRepo;
    private final DtoUtil<RoleEntity, RoleReqDto, RoleResDto> dtoUtil;
    private final PaginationDtoUtil<RoleEntity, RoleReqDto, RoleResDto> paginationDtoUtil;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, AuthorityRepo authorityRepo, DtoUtil<RoleEntity, RoleReqDto, RoleResDto> dtoUtil, PaginationDtoUtil<RoleEntity, RoleReqDto, RoleResDto> paginationDtoUtil) {
        this.roleRepo = roleRepo;
        this.authorityRepo = authorityRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('ROLE_C')")
    @Override
    public RoleResDto save(RoleReqDto roleReqDto) {
        LOG.info("----- Saving Role. -----");
        if (roleRepo.findByTitle(roleReqDto.getTitle()).isPresent()) {
            throw new CustomException("ROL002");
        }

        RoleEntity role = dtoUtil.reqToEntity(roleReqDto);
        if (roleReqDto.getAuthorityIds() != null && roleReqDto.getAuthorityIds().size() > 0) {
            role.setAuthorities(
                    roleReqDto.getAuthorityIds().stream().map(id -> {
                        Optional<AuthorityEntity> optionalAuthorityEntity = authorityRepo.findById(id);
                        optionalAuthorityEntity.orElseThrow(() -> new CustomException("AUT002"));
                        return optionalAuthorityEntity.get();
                    }).collect(Collectors.toList())
            );
        }
        return dtoUtil.prepRes(roleRepo.save(role));
    }

    @PreAuthorize("hasAuthority('ROLE_RA')")
    @Override
    public PaginationResDto<RoleResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Role. -----");

        List<String> fields = Arrays.asList("title");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReqDto, fields, sortBy, sortOrder, roleRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('ROLE_U')")
    @Override
    public RoleResDto update(RoleReqDto roleReqDto) {
        LOG.info("----- Updating Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleReqDto.getId());
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
        RoleEntity roleEntity = optionalRoleEntity.get();

        if (roleReqDto.getTitle() != null && !roleReqDto.getTitle().equalsIgnoreCase(roleEntity.getTitle())
                && roleRepo.findByTitle(roleReqDto.getTitle()).isPresent()) {
            throw new CustomException("ROL002");
        }
        if (roleReqDto.getAuthorityIds() != null) {

            //Add authorityIds
            List<AuthorityEntity> newAuthorities = new ArrayList<>();
            for (Long authorityId : roleReqDto.getAuthorityIds()) {
                Optional<AuthorityEntity> optionalAuthorityEntity = authorityRepo.findById(authorityId);
                optionalAuthorityEntity.orElseThrow(() -> new CustomException("AUT002"));
                AuthorityEntity authorityEntity = optionalAuthorityEntity.get();

                newAuthorities.add(authorityEntity);
                if (!roleEntity.getAuthorities().contains(authorityEntity)) {
                    roleEntity.getAuthorities().add(authorityEntity);
                }
            }

            //Remove authorityIds
            List<AuthorityEntity> authoritiesToRemove = new ArrayList<>();
            for (AuthorityEntity authorityEntity : roleEntity.getAuthorities()) {
                if (!newAuthorities.contains(authorityEntity)) {
                    authoritiesToRemove.add(authorityEntity);
                }
            }
            roleEntity.getAuthorities().removeAll(authoritiesToRemove);
        }
        dtoUtil.setUpdatedValue(roleReqDto, roleEntity);
        return dtoUtil.prepRes(roleEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(id);
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

        roleRepo.delete(optionalRoleEntity.get());
    }
}
