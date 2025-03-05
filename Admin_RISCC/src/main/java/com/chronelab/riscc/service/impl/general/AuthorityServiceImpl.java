package com.chronelab.riscc.service.impl.general;

import com.chronelab.riscc.dto.PaginationDtoUtil;
import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.PaginationResDto;
import com.chronelab.riscc.dto.request.general.AuthorityReqDto;
import com.chronelab.riscc.dto.response.general.AuthorityResDto;
import com.chronelab.riscc.dto.util.DtoUtil;
import com.chronelab.riscc.entity.general.AuthorityEntity;
import com.chronelab.riscc.repo.general.AuthorityRepo;
import com.chronelab.riscc.service.general.AuthorityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger LOG = LogManager.getLogger();

    private final AuthorityRepo authorityRepo;
    private final DtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto> dtoUtil;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepo authorityRepo, DtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto> dtoUtil) {
        this.authorityRepo = authorityRepo;
        this.dtoUtil = dtoUtil;
    }

    @PreAuthorize("hasAuthority('AUTHORITY')")
    @Transactional(readOnly = true)
    @Override
    public PaginationResDto<AuthorityResDto> get(PaginationReqDto paginationReqDto) {
        LOG.info("----- Getting paginated Authority. -----");

        List<String> fields = Arrays.asList("title");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return new PaginationDtoUtil<AuthorityEntity, AuthorityReqDto, AuthorityResDto>().paginate(paginationReqDto, fields, sortBy, sortOrder, authorityRepo, dtoUtil);
    }
}
